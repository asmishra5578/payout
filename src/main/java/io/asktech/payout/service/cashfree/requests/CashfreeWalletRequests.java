package io.asktech.payout.service.cashfree.requests;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.paytm.pg.merchant.PaytmChecksum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.asktech.payout.constants.ErrorValues;
import io.asktech.payout.constants.PayTMConstants;
import io.asktech.payout.constants.PaytmWalletConstants;
import io.asktech.payout.dto.merchant.AccountTransferMerReq;
import io.asktech.payout.dto.merchant.AccountTransferUPIMerReq;
import io.asktech.payout.dto.merchant.BankAccountVerifyRequest;
import io.asktech.payout.dto.merchant.TransactionReportMerReq;
import io.asktech.payout.dto.merchant.TransactionReportMerReqResult;
import io.asktech.payout.dto.merchant.TransactionReportMerRes;
import io.asktech.payout.dto.merchant.TransactionResponseMerRes;
import io.asktech.payout.dto.merchant.WalletTransferMerReq;
import io.asktech.payout.dto.reqres.TransferStatusReq;
import io.asktech.payout.dto.reqres.VanCreationResError;
import io.asktech.payout.dto.reqres.WalletTransferRes;
import io.asktech.payout.dto.reqres.cashfree.AccountTransferData;
import io.asktech.payout.dto.reqres.cashfree.AccountTransferReq;
import io.asktech.payout.dto.reqres.cashfree.AccountTransferRes;
import io.asktech.payout.dto.reqres.cashfree.AuthenticationRes;
import io.asktech.payout.dto.reqres.cashfree.StatusApi;
import io.asktech.payout.dto.reqres.cashfree.UPITransferDataReq;
import io.asktech.payout.dto.reqres.cashfree.UPITransferReq;
import io.asktech.payout.dto.reqres.cashfree.UPITransferRes;
import io.asktech.payout.enums.FormValidationExceptionEnums;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.modal.merchant.ApiRequestResponseLogger;
import io.asktech.payout.modal.merchant.MerchantWalletDetails;
import io.asktech.payout.modal.merchant.PgDetails;
import io.asktech.payout.modal.merchant.TransactionDetails;
import io.asktech.payout.paytm.requests.dto.PayTmTransactionReportReq;
import io.asktech.payout.paytm.requests.dto.PayTmTransactionReportRes;
import io.asktech.payout.paytm.requests.dto.PayTmTransactionReportResResultResult;
import io.asktech.payout.paytm.requests.dto.PaytmAccountVerifyRequest;
import io.asktech.payout.paytm.requests.dto.PaytmAccountVerifyResponse;
import io.asktech.payout.paytm.requests.dto.PaytmWalletTransferRequestDto;
import io.asktech.payout.repository.merchant.MerchantWalletDetailsRepo;
import io.asktech.payout.repository.merchant.TransactionDetailsRepo;
import io.asktech.payout.repository.reqres.ApiRequestResponseLoggerRepo;
import io.asktech.payout.service.cashfree.dto.error;
import io.asktech.payout.utils.Utility;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

@Component
public class CashfreeWalletRequests implements PayTMConstants, ErrorValues {

	static Logger logger = LoggerFactory.getLogger(CashfreeWalletRequests.class);

	@Autowired
	MerchantWalletDetailsRepo merchantWalletDetailsRepo;
	@Autowired
	TransactionDetailsRepo transactionDetailsRepo;

	@Autowired
	ApiRequestResponseLoggerRepo apiRequestResponseLoggerRepo;

	@Value("${payTmCredentials.wallet.walletBaseTransferURL}")
	String walletBaseTransferURL;
	@Value("${payTmCredentials.wallet.bankAccountTransferBaseUrl}")
	String bankAccountTransferBaseUrl;
	@Value("${payTmCredentials.wallet.transferStatusURL}")
	String transferStatusURL;
	@Value("${payTmCredentials.wallet.getWalletBalance}")
	String getWalletBalance;
	@Value("${payTmCredentials.wallet.transactionReportWallet}")
	String transactionReportWallet;
	@Value("${payTmCredentials.wallet.accountVerify}")
	String accountVerify;
	@Value("${payTmCredentials.wallet.accountVerifyCallBackApi}")
	String accountVerifyCallBackApi;
	@Value("${payTmCredentials.wallet.walletMid}")
	String walletMid;
	@Value("${payTmCredentials.wallet.walletKey}")
	String walletKey;
	@Value("${cashfreeCredentials.authUrl}")
	String authUrl;

	private String ClientId;

	// @Value("${cashfreeCredentials.creds.XClientSecret}")
	private String ClientSecret;

	@Value("${cashfreeCredentials.directtransferUrl}")
	String transferURL;

	public void setClientDetails(String ClientId, String ClientSecret) {
		this.ClientId = ClientId;
		this.ClientSecret = ClientSecret;
	}

	public WalletTransferRes doWalletTransfer(WalletTransferMerReq dto, String merchantid)
			throws ValidationExceptions, JsonProcessingException {
		logger.info("Inside doWalletTransfer() :::");
		logger.info("Request Transfer for Merchant Id :: " + merchantid);

		MerchantWalletDetails Guuidno = merchantWalletDetailsRepo.findByMerchantId(merchantid);
		if (Guuidno == null) {
			throw new ValidationExceptions(ACCOUNT_NOT_FOUND_E0102, FormValidationExceptionEnums.BANK_ERROR);
		}

		PaytmWalletTransferRequestDto walletTransferRequestDto = new PaytmWalletTransferRequestDto(
				dto.getInternalOrderId(), Guuidno.getWalletGuuid(), String.valueOf(dto.getAmount()),
				dto.getPhonenumber());
		String post_data = Utility.convertDTO2JsonString(walletTransferRequestDto).toString();

		String checksum = generatePaytmCheckSum(post_data);

		try {
			HttpResponse<WalletTransferRes> walletTransferRes = Unirest.post(walletBaseTransferURL)
					.header(CONTENTTYPE, APPTYPE).header("x-mid", walletMid).header("x-checksum", checksum)
					.body(walletTransferRequestDto).asObject(WalletTransferRes.class)
					.ifFailure(error.class, r -> {
						error e = r.getBody();
					});
			return walletTransferRes.getBody();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new ValidationExceptions(PAYTM_SYSTEM_EXCEPTION_ES1001, FormValidationExceptionEnums.ES1001);
		}

	}

	public WalletTransferRes doAccountTransfer(AccountTransferMerReq dto, String merchantid, PgDetails pg)
			throws ValidationExceptions, JsonProcessingException, ParseException {

		/*
		 * MerchantWalletDetails Guuidno =
		 * merchantWalletDetailsRepo.findByMerchantId(merchantid); if (Guuidno == null)
		 * { throw new ValidationExceptions(ACCOUNT_NOT_FOUND_E0102,
		 * FormValidationExceptionEnums.BANK_ERROR); }
		 */
		setClientDetails(pg.getPgConfigKey(), pg.getPgConfigSecret());
		AccountTransferReq accountTransferReq = new AccountTransferReq();
		accountTransferReq.setAmount(String.valueOf(dto.getAmount()));
		accountTransferReq.setTransferMode(dto.getRequestType().toLowerCase());
		accountTransferReq.setTransferId(dto.getInternalOrderid());
		accountTransferReq.setRemarks(dto.getPurpose().replaceAll("_", " "));
		AccountTransferData accountTransferData = new AccountTransferData();
		accountTransferData.setPhone(dto.getPhonenumber());
		accountTransferData.setAddress1("Inside trades Pvt Ltd");
		accountTransferData.setName(dto.getBeneficiaryName());
		accountTransferData.setBankAccount(dto.getBankaccount());
		accountTransferData.setIfsc(dto.getIfsc());
		accountTransferData.setEmail("supportSystems@gmail.com");
		accountTransferReq.setBeneDetails(accountTransferData);
		logger.info("Payload for Account Transfer Request to Cashfree :: "
				+ Utility.convertDTO2JsonString(accountTransferReq));

		String AuthToken = "Token";
		try {
			AuthenticationRes details = cashfreeAuthToken();
			AuthToken = details.getData().getToken();
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		ApiRequestResponseLogger apiRequestResponseLogger = new ApiRequestResponseLogger();
		apiRequestResponseLogger.setMerchantId(merchantid);
		apiRequestResponseLogger.setRequestId(dto.getInternalOrderid());
		apiRequestResponseLogger.setRequest(Utility.convertDTO2JsonString(accountTransferReq));
		apiRequestResponseLogger.setServiceProvider(pg.getPgName());

		apiRequestResponseLoggerRepo.save(apiRequestResponseLogger);
		WalletTransferRes walletTransferRes = new WalletTransferRes();
		if (AuthToken != null) {

			HttpResponse<AccountTransferRes> TransferRes = Unirest.post(transferURL).header(CONTENTTYPE, APPTYPE)
					.header("Authorization", "Bearer " + AuthToken).body(accountTransferReq)
					.asObject(AccountTransferRes.class).ifFailure(VanCreationResError.class, r -> {
						VanCreationResError e = r.getBody();
						try {
							logger.error("Cashfree Error" + Utility.convertDTO2JsonString(e));
							apiRequestResponseLogger.setErrorStatus(Utility.convertDTO2JsonString(e));
							apiRequestResponseLoggerRepo.save(apiRequestResponseLogger);
						} catch (JsonProcessingException e1) { // TODO Auto-generated catch block
							logger.error(e1.getMessage());
							e1.printStackTrace();
						}
					});

			logger.info("Response from Cashfree :: " + Utility.convertDTO2JsonString(TransferRes.getBody()));
			apiRequestResponseLogger.setResponse(Utility.convertDTO2JsonString(TransferRes.getBody()));
			apiRequestResponseLoggerRepo.save(apiRequestResponseLogger);

			AccountTransferRes res = TransferRes.getBody();
			if (res.getStatus().equalsIgnoreCase("PENDING")) {
				walletTransferRes.setStatus("PENDING");
				walletTransferRes.setStatusMessage(res.getMessage());
				walletTransferRes.setReferenceId(res.getData().getReferenceId());
				walletTransferRes.setUtr(res.getData().getUtr());
			} else if (res.getStatus().equalsIgnoreCase("SUCCESS")) {
				walletTransferRes.setStatus("SUCCESS");
				walletTransferRes.setStatusMessage(res.getMessage());
				walletTransferRes.setReferenceId(res.getData().getReferenceId());
				walletTransferRes.setUtr(res.getData().getUtr());
			} else if (res.getStatus().equalsIgnoreCase("ERROR")) {
				walletTransferRes.setStatus("FAILURE");
				walletTransferRes.setStatusMessage(res.getMessage());
				if (res.getData() != null) {
					walletTransferRes.setReferenceId(res.getData().getReferenceId());
					walletTransferRes.setUtr(res.getData().getUtr());
				}
			} else {
				walletTransferRes.setStatus(res.getStatus());
				walletTransferRes.setStatusMessage(res.getMessage());
				if (res.getData() != null) {
					walletTransferRes.setReferenceId(res.getData().getReferenceId());
					walletTransferRes.setUtr(res.getData().getUtr());
				}
			}
			walletTransferRes.setStatusCode(res.getSubCode());
			// walletTransferRes.setPgId(pg.getPgId());
			// String[] sts = { "SUCCESS", "FAILURE", "PENDING" };
			// Random r = new Random();
			// int randomNumber = r.nextInt(sts.length);
			// walletTransferRes.setStatus(sts[randomNumber]);
			// walletTransferRes.setStatusMessage("Request Accepted");
			// walletTransferRes.setStatusMessage("Test ACCOUNT");
			// walletTransferRes.setStatusCode("1234");
			// walletTransferRes.setReferenceId(Utility.getRandomId());
			// walletTransferRes.setUtr("ACCNUMBER");
			return walletTransferRes;
		}
		walletTransferRes.setStatus("FAILURE");
		walletTransferRes.setStatusMessage("Auth ERROR");
		// WalletTransferRes.setPgId(pg.getPgId());
		return walletTransferRes;
	}

	public WalletTransferRes doAccountTransferUPI(AccountTransferUPIMerReq dto, String merchantid, PgDetails pg)
			throws ValidationExceptions, JsonProcessingException, ParseException {
		setClientDetails(pg.getPgConfigKey(), pg.getPgConfigSecret());
		/*
		 * MerchantWalletDetails Guuidno =
		 * merchantWalletDetailsRepo.findByMerchantId(merchantid);
		 * 
		 * if (Guuidno == null) { throw new
		 * ValidationExceptions(ACCOUNT_NOT_FOUND_E0102,
		 * FormValidationExceptionEnums.BANK_ERROR); }
		 */

		UPITransferReq upiTransferReq = new UPITransferReq();
		upiTransferReq.setAmount(String.valueOf(dto.getAmount()));
		upiTransferReq.setTransferMode(dto.getRequestType().toLowerCase());
		upiTransferReq.setTransferId(dto.getInternalOrderid());
		upiTransferReq.setRemarks(dto.getPurpose().replaceAll("_", " "));
		UPITransferDataReq upiTransferDataReq = new UPITransferDataReq();
		upiTransferDataReq.setVpa(dto.getBeneficiaryVPA());
		if (dto.getPhonenumber() == null) {
			dto.setPhonenumber("9999999999");
		}
		upiTransferDataReq.setPhone(dto.getPhonenumber());
		upiTransferDataReq.setAddress1("Support Business Pvt Ltd");
		if (dto.getBeneficiaryName() == null) {
			dto.setBeneficiaryName("Rahul");
		}
		upiTransferDataReq.setName(dto.getBeneficiaryName());
		upiTransferDataReq.setEmail("supportsystems@gmail.com");
		upiTransferReq.setBeneDetails(upiTransferDataReq);

		logger.info("Payload for UPI Transfer Request to Cashfree :: " + Utility.convertDTO2JsonString(upiTransferReq));

		String AuthToken = "Token";
		try {
			AuthenticationRes details = cashfreeAuthToken();
			AuthToken = details.getData().getToken();
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		ApiRequestResponseLogger apiRequestResponseLogger = new ApiRequestResponseLogger();
		apiRequestResponseLogger.setMerchantId(merchantid);
		apiRequestResponseLogger.setRequestId(dto.getInternalOrderid());
		apiRequestResponseLogger.setRequest(Utility.convertDTO2JsonString(upiTransferReq));
		apiRequestResponseLogger.setServiceProvider("CASHFREE");

		apiRequestResponseLoggerRepo.save(apiRequestResponseLogger);

		WalletTransferRes walletTransferRes = new WalletTransferRes();
		if (AuthToken != null) {

			HttpResponse<UPITransferRes> upiTransferRes = Unirest.post(transferURL).header(CONTENTTYPE, APPTYPE)
					.header("Authorization", "Bearer " +
							AuthToken)
					.body(upiTransferReq).asObject(UPITransferRes.class)
					.ifFailure(VanCreationResError.class, r -> {
						VanCreationResError e = r.getBody();
						try {
							logger.error("Cashfree Error" +
									Utility.convertDTO2JsonString(e));
							apiRequestResponseLogger.setErrorStatus(Utility.convertDTO2JsonString(e));
							apiRequestResponseLoggerRepo.save(apiRequestResponseLogger);
						} catch (JsonProcessingException e1) { // TODO Auto-generated catch block
							logger.error(e1.getMessage());
							e1.printStackTrace();
						}
					});

			logger.info("Response from cashfree :: " +
					Utility.convertDTO2JsonString(upiTransferRes.getBody()));

			apiRequestResponseLogger.setResponse(Utility.convertDTO2JsonString(
					upiTransferRes.getBody()));
			apiRequestResponseLoggerRepo.save(apiRequestResponseLogger);
			UPITransferRes res = upiTransferRes.getBody();
			if (res.getStatus().equalsIgnoreCase("PENDING")) {
				walletTransferRes.setStatus("PENDING");
				walletTransferRes.setStatusMessage(res.getMessage());
				walletTransferRes.setReferenceId(res.getData().getReferenceId());
				walletTransferRes.setUtr(res.getData().getUtr());
			} else if (res.getStatus().equalsIgnoreCase("SUCCESS")) {
				walletTransferRes.setStatus("SUCCESS");
				walletTransferRes.setStatusMessage(res.getMessage());
				walletTransferRes.setReferenceId(res.getData().getReferenceId());
				walletTransferRes.setUtr(res.getData().getUtr());
			} else if (res.getStatus().equalsIgnoreCase("ERROR")) {
				walletTransferRes.setStatus("FAILURE");
				walletTransferRes.setStatusMessage(res.getMessage());
				if (res.getData() != null) {
					walletTransferRes.setReferenceId(res.getData().getReferenceId());
					walletTransferRes.setUtr(res.getData().getUtr());
				}
			} else {
				walletTransferRes.setStatus(res.getStatus());
				walletTransferRes.setStatusMessage(res.getMessage());
				if (res.getData() != null) {
					walletTransferRes.setReferenceId(res.getData().getReferenceId());
					walletTransferRes.setUtr(res.getData().getUtr());
				}
			}
			walletTransferRes.setStatusCode(res.getSubCode());

			// String[] sts = { "SUCCESS", "FAILURE", "PENDING" };
			// Random r = new Random();
			// int randomNumber = r.nextInt(sts.length);
			// walletTransferRes.setStatus(sts[randomNumber]);
			// walletTransferRes.setStatusMessage("Request Accepted");
			// walletTransferRes.setStatusMessage("Test ACCOUNT");
			// walletTransferRes.setStatusCode("1234");
			// walletTransferRes.setReferenceId(Utility.getRandomId());
			// walletTransferRes.setUtr("ACCNUMBER");
			return walletTransferRes;
		}
		walletTransferRes.setStatus("FAILURE");
		walletTransferRes.setStatusMessage("Auth ERROR");
		return walletTransferRes;
	}

	public TransactionResponseMerRes doWalletTransferStatus(TransferStatusReq dto, String merchantid,
			TransactionDetails transactionDetails, PgDetails pg)
			throws JsonProcessingException, ValidationExceptions {
		setClientDetails(pg.getPgConfigKey(), pg.getPgConfigSecret());
		TransferStatusReq transferStatusReq = new TransferStatusReq();
		transferStatusReq.setOrderId(dto.getOrderId());
		ApiRequestResponseLogger apiRequestResponseLogger = new ApiRequestResponseLogger();
		apiRequestResponseLogger.setMerchantId(merchantid);
		apiRequestResponseLogger.setRequestId(dto.getOrderId());
		apiRequestResponseLogger.setRequest(Utility.convertDTO2JsonString(transferStatusReq));
		apiRequestResponseLogger.setServiceProvider(pg.getPgName());

		apiRequestResponseLoggerRepo.save(apiRequestResponseLogger);
		String referenceId = null;
		// String referenceId = transactionDetailsRepo.find(dto.getOrderId());
		logger.info(dto.getOrderId());
		// TransactionDetails transactionDetails =
		// transactionDetailsRepo.findByOrderId(dto.getOrderId());
		referenceId = transactionDetails.getReferenceId();
		TransactionResponseMerRes transactionResponseMerRes = new TransactionResponseMerRes();
		transactionResponseMerRes.setAmount(transactionDetails.getAmount());
		transactionResponseMerRes.setOrderid(transactionDetails.getOrderId());

		if (referenceId != null) {

			String AuthToken = null;
			try {
				AuthenticationRes details = cashfreeAuthToken();
				AuthToken = details.getData().getToken();
			} catch (Exception e) {
				logger.info(e.getMessage());
				transactionResponseMerRes.setStatus("ERROR");
				transactionResponseMerRes.setStatusMessage("Authentication Error Occured");
				return transactionResponseMerRes;
			}
			HttpResponse<StatusApi> statusRes = Unirest
					.get("https://payout-api.cashfree.com/payout/v1/getTransferStatus?referenceId=" + referenceId
							+ "&transferId=" + dto.getOrderId())
					.header(CONTENTTYPE, APPTYPE).header("Authorization", "Bearer " + AuthToken)
					.asObject(StatusApi.class).ifFailure(VanCreationResError.class, r -> {
						VanCreationResError e = r.getBody();
						try {
							logger.error("Cashfree Error" + Utility.convertDTO2JsonString(e));
							apiRequestResponseLogger.setErrorStatus(Utility.convertDTO2JsonString(e));
							apiRequestResponseLoggerRepo.save(apiRequestResponseLogger);
						} catch (JsonProcessingException e1) {
							// TODO Auto-generated catch block
							logger.error(e1.getMessage());
							e1.printStackTrace();
						}
					});
			StatusApi st = statusRes.getBody();
			logger.info("CASHFREE RESPONSE::" + Utility.convertDTO2JsonString(st));
			apiRequestResponseLogger.setResponse(Utility.convertDTO2JsonString(st));
				apiRequestResponseLoggerRepo.save(apiRequestResponseLogger);
			if (st.getStatus().equalsIgnoreCase("SUCCESS")) {

				transactionResponseMerRes.setStatus(st.getData().getTransfer().getStatus());
				transactionResponseMerRes.setMessage("");
				if (st.getData().getTransfer().getUtr() != null) {
					transactionResponseMerRes.setUtrId(st.getData().getTransfer().getUtr());
				}
				transactionResponseMerRes.setStatusMessage("Fetch Success");
				return transactionResponseMerRes;
			} else {
				transactionResponseMerRes.setStatus(st.getStatus());
				if (st.getMessage() != null) {
					transactionResponseMerRes.setStatusMessage(st.getMessage());
					return transactionResponseMerRes;
				} else {
					transactionResponseMerRes.setStatus("ERROR");
					transactionResponseMerRes.setStatusMessage("Unable to fetch details");
					return transactionResponseMerRes;
				}
			}

		} else {
			transactionResponseMerRes.setStatus(transactionDetails.getTransactionStatus());
			transactionResponseMerRes.setStatusMessage(transactionDetails.getTransactionMessage());
			return transactionResponseMerRes;
		}

	}

	public PaytmAccountVerifyResponse verifyBankAccount(BankAccountVerifyRequest dto)
			throws ValidationExceptions, JsonProcessingException {

		MerchantWalletDetails Guuidno = merchantWalletDetailsRepo.findByMerchantId(dto.getMerchantId());
		if (Guuidno == null) {
			throw new ValidationExceptions(ACCOUNT_NOT_FOUND_E0102, FormValidationExceptionEnums.BANK_ERROR);
		}

		PaytmAccountVerifyRequest paytmAccountVerifyRequest = new PaytmAccountVerifyRequest();
		paytmAccountVerifyRequest.setBeneficiaryAccount(dto.getBeneficiaryAccountNo());
		paytmAccountVerifyRequest.setBeneficiaryIFSC(dto.getBeneficiaryIFSCCode());
		paytmAccountVerifyRequest.setOrderId(dto.getInternalOrderId());
		paytmAccountVerifyRequest.setSubwalletGuid(Guuidno.getWalletGuuid());
		paytmAccountVerifyRequest.setCallbackUrl(accountVerifyCallBackApi);
		paytmAccountVerifyRequest.setBeneficiaryVPA(dto.getBeneficiaryVPA());

		String post_data = Utility.convertDTO2JsonString(paytmAccountVerifyRequest).toString();
		String checksum = generatePaytmCheckSum(post_data);

		logger.info("Input payload :: " + post_data);

		PaytmAccountVerifyResponse paytmAccountVerifyResponse = Unirest.post(accountVerify).header(CONTENTTYPE, APPTYPE)
				.header("x-mid", walletMid).header("x-checksum", checksum).body(paytmAccountVerifyRequest)
				.asObject(PaytmAccountVerifyResponse.class).ifFailure(VanCreationResError.class, r -> {
					VanCreationResError e = r.getBody();
				}).getBody();

		return paytmAccountVerifyResponse;
	}

	/*
	 * public BalanceCheckMerRes getBalance(String merchantid) throws
	 * JsonProcessingException, ValidationExceptions {
	 * 
	 * MerchantWalletDetails Guuidno =
	 * merchantWalletDetailsRepo.findByMerchantId(merchantid);
	 * logger.info(Utility.convertDTO2JsonString(Guuidno)); if (Guuidno == null) {
	 * throw new ValidationExceptions(ACCOUNT_NOT_FOUND_E0102,
	 * FormValidationExceptionEnums.BANK_ERROR); }
	 * 
	 * PayTmBalanceCheckReq payTmBalanceCheckReq = new PayTmBalanceCheckReq();
	 * payTmBalanceCheckReq.setSubwalletGuid(Guuidno.getWalletGuuid());
	 * 
	 * String post_data =
	 * Utility.convertDTO2JsonString(payTmBalanceCheckReq).toString(); String
	 * checksum = generatePaytmCheckSum(post_data);
	 * logger.info(Utility.convertDTO2JsonString(payTmBalanceCheckReq));
	 * PayTmBalanceCheckRes payTmBalanceCheckRes =
	 * Unirest.post(getWalletBalance).header(CONTENTTYPE, APPTYPE) .header("x-mid",
	 * walletMid).header("x-checksum", checksum).body(payTmBalanceCheckReq)
	 * .asObject(PayTmBalanceCheckRes.class).ifFailure(VanCreationResError.class, r
	 * -> { VanCreationResError e = r.getBody(); }).getBody();
	 * logger.info(Utility.convertDTO2JsonString(payTmBalanceCheckRes));
	 * BalanceCheckMerRes balanceCheckMerRes = new BalanceCheckMerRes(); if
	 * (payTmBalanceCheckRes.getStatus().equals(PaytmWalletConstants.SUCCESS)) {
	 * PayTmBalanceCheckResults payTmBalanceCheckResults =
	 * payTmBalanceCheckRes.getResult().get(0);
	 * balanceCheckMerRes.setLastUpdatedDate(payTmBalanceCheckResults.
	 * getLastUpdatedDate());
	 * balanceCheckMerRes.setWalletBalance(payTmBalanceCheckResults.getWalletBalance
	 * ()); } balanceCheckMerRes.setMerchantId(merchantid);
	 * balanceCheckMerRes.setMessage(payTmBalanceCheckRes.getStatusMessage());
	 * balanceCheckMerRes.setStatus(payTmBalanceCheckRes.getStatus());
	 * 
	 * return balanceCheckMerRes; }
	 */

	public TransactionReportMerRes getTransactionReport(TransactionReportMerReq dto, String merchantId)
			throws JsonProcessingException, ValidationExceptions {

		MerchantWalletDetails Guuidno = merchantWalletDetailsRepo.findByMerchantId(merchantId);
		if (Guuidno == null) {
			throw new ValidationExceptions(ACCOUNT_NOT_FOUND_E0102, FormValidationExceptionEnums.BANK_ERROR);
		}

		PayTmTransactionReportReq payTmTransactionReportReq = new PayTmTransactionReportReq();
		payTmTransactionReportReq.setSubwalletGuid(Guuidno.getWalletGuuid());
		payTmTransactionReportReq.setFromDate(dto.getFromDate());
		payTmTransactionReportReq.setToDate(dto.getToDate());
		payTmTransactionReportReq.setPageSize(10000);

		String post_data = Utility.convertDTO2JsonString(payTmTransactionReportReq).toString();

		String checksum = generatePaytmCheckSum(post_data);

		PayTmTransactionReportRes payTmTransactionReportRes = Unirest.post(transactionReportWallet)
				.header(CONTENTTYPE, APPTYPE).header("x-mid", walletMid).header("x-checksum", checksum)
				.body(payTmTransactionReportReq).asObject(PayTmTransactionReportRes.class)
				.ifFailure(VanCreationResError.class, r -> {
					VanCreationResError e = r.getBody();
				}).getBody();

		TransactionReportMerRes transactionReportMerRes = new TransactionReportMerRes();
		transactionReportMerRes.setStatus(payTmTransactionReportRes.getStatus());
		transactionReportMerRes.setStatusMessage(payTmTransactionReportRes.getStatusMessage());

		if (payTmTransactionReportRes.getStatus().equals(PaytmWalletConstants.SUCCESS)) {

			List<TransactionReportMerReqResult> transactionReportMerReqResults = new ArrayList<>();
			for (PayTmTransactionReportResResultResult rs : payTmTransactionReportRes.getResult().getResult()) {
				System.out.println(Utility.convertDTO2JsonString(rs));
				TransactionReportMerReqResult transactionReportMerReqResult = new TransactionReportMerReqResult();
				transactionReportMerReqResult.setBeneficiary(rs.getBeneficiary());
				transactionReportMerReqResult.setCreditOrDebit(rs.getCreditOrDebit());
				transactionReportMerReqResult.setTxnId(rs.getTxnId());
				transactionReportMerReqResult.setMerchantId(merchantId);
				transactionReportMerReqResult.setMerchantTxnType(rs.getMerchantTxnType());
				transactionReportMerReqResult.setOrderId(rs.getOrderId());
				transactionReportMerReqResult.setTxnAmount(rs.getTxnAmount());
				transactionReportMerReqResult.setTxnDate(rs.getTxnDate());
				transactionReportMerReqResult.setTxnType(rs.getTxnType());
				transactionReportMerReqResult.setMerchantTxnType(rs.getMerchantTxnType());
				transactionReportMerReqResult.setTxnStatus(rs.getTxnStatus());
				transactionReportMerReqResults.add(transactionReportMerReqResult);
			}

			transactionReportMerRes.setResult(transactionReportMerReqResults);
		}
		return transactionReportMerRes;

	}

	public String generatePaytmCheckSum(String post_data) throws ValidationExceptions {
		String checksum = null;
		try {
			checksum = PaytmChecksum.generateSignature(post_data, walletKey);
		} catch (Exception e1) {
			throw new ValidationExceptions(PAYTM_CHECKSUM_ERROR_E0101, FormValidationExceptionEnums.E0101);
		}

		if (Objects.isNull(checksum)) {
			throw new ValidationExceptions(PAYTM_CHECKSUM_ERROR_E0101, FormValidationExceptionEnums.E0101);
		}

		return checksum;
	}

	public AuthenticationRes cashfreeAuthToken() throws JsonProcessingException {
		logger.info("Cashfree Auth Request|" + ClientId + "|" + ClientSecret);
		AuthenticationRes authenticationRes = Unirest.post(authUrl).header("X-Client-Id", ClientId)
				.header("X-Client-Secret", ClientSecret).asObject(AuthenticationRes.class)
				.ifFailure(VanCreationResError.class, r -> {
					VanCreationResError e = r.getBody();
				}).getBody();
		logger.info(Utility.convertDTO2JsonString(authenticationRes));
		return authenticationRes;

	}

	/*
	 *
	 * public TransferFundsRes doTransferFunds(TransferFundsReq dto) throws
	 * Exception {
	 *
	 * PaytmTransferFundsRequestDto transferFundsRequestDto = new
	 * PaytmTransferFundsRequestDto(dto.getAmount(), GUUID
	 *
	 * ); String post_data = transferFundsRequestDto.toString(); String checksum =
	 * PaytmChecksum.generateSignature(post_data, merchantKEYUAT);
	 *
	 * HttpResponse<TransferFundsRes> transFundRes =
	 * Unirest.post(WalletBaseURL).header(CONTENTTYPE, APPTYPE) .header("x-mid",
	 * x_midUAT).header("x-checksum", checksum).body(transferFundsRequestDto)
	 * .asObject(TransferFundsRes.class).ifFailure(VanCreationResError.class, r -> {
	 * VanCreationResError e = r.getBody();
	 *
	 * });
	 *
	 * return transFundRes.getBody();
	 *
	 * }
	 */
}
