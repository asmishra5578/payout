package io.asktech.payout.service.paytm.requests;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.paytm.pg.merchant.PaytmChecksum;

import io.asktech.payout.constants.ErrorValues;
import io.asktech.payout.constants.PayTMConstants;
import io.asktech.payout.constants.PaytmWalletConstants;
import io.asktech.payout.dto.merchant.AccountTransferMerReq;
import io.asktech.payout.dto.merchant.AccountTransferUPIMerReq;
import io.asktech.payout.dto.merchant.BalanceCheckMerRes;
import io.asktech.payout.dto.merchant.BankAccountVerifyRequest;
import io.asktech.payout.dto.merchant.TransactionReportMerReq;
import io.asktech.payout.dto.merchant.TransactionReportMerReqResult;
import io.asktech.payout.dto.merchant.TransactionReportMerRes;
import io.asktech.payout.dto.merchant.TransactionResponseMerRes;
import io.asktech.payout.dto.merchant.WalletTransferMerReq;
import io.asktech.payout.dto.reqres.TransferStatusReq;
import io.asktech.payout.dto.reqres.TransferStatusRes;
import io.asktech.payout.dto.reqres.VanCreationResError;
import io.asktech.payout.dto.reqres.WalletTransferRes;
import io.asktech.payout.enums.FormValidationExceptionEnums;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.modal.merchant.MerchantWalletDetails;
import io.asktech.payout.modal.merchant.TransactionDetails;
import io.asktech.payout.paytm.requests.dto.PayTmAccountRequestsDto;
import io.asktech.payout.paytm.requests.dto.PayTmBalanceCheckReq;
import io.asktech.payout.paytm.requests.dto.PayTmBalanceCheckRes;
import io.asktech.payout.paytm.requests.dto.PayTmBalanceCheckResults;
import io.asktech.payout.paytm.requests.dto.PayTmTransactionReportReq;
import io.asktech.payout.paytm.requests.dto.PayTmTransactionReportRes;
import io.asktech.payout.paytm.requests.dto.PayTmTransactionReportResResultResult;
import io.asktech.payout.paytm.requests.dto.PaytmAccountVerifyRequest;
import io.asktech.payout.paytm.requests.dto.PaytmAccountVerifyResponse;
import io.asktech.payout.paytm.requests.dto.PaytmTransferStatusRequestDto;
import io.asktech.payout.paytm.requests.dto.PaytmWalletTransferRequestDto;
import io.asktech.payout.repository.merchant.MerchantWalletDetailsRepo;
import io.asktech.payout.repository.merchant.TransactionDetailsRepo;
import io.asktech.payout.utils.Utility;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

@Component
public class PaytmWalletRequests implements PayTMConstants , ErrorValues{

	static Logger logger = LoggerFactory.getLogger(PaytmWalletRequests.class);
	
	@Autowired
	MerchantWalletDetailsRepo merchantWalletDetailsRepo;
	@Autowired
	TransactionDetailsRepo transactionDetailsRepo;
	
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
	

	public WalletTransferRes doWalletTransfer(WalletTransferMerReq dto, String merchantid)
			throws ValidationExceptions, JsonProcessingException {
		logger.info("Inside doWalletTransfer() :::");
		logger.info("Request Transfer for Merchant Id :: "+merchantid);	
		
		MerchantWalletDetails Guuidno = merchantWalletDetailsRepo.findByMerchantId(merchantid);
		if(Guuidno == null) {
			throw new ValidationExceptions(ACCOUNT_NOT_FOUND_E0102, FormValidationExceptionEnums.BANK_ERROR);
		}
		
		PaytmWalletTransferRequestDto walletTransferRequestDto = new PaytmWalletTransferRequestDto(dto.getInternalOrderId(),
				Guuidno.getWalletGuuid(), String.valueOf(dto.getAmount()), dto.getPhonenumber());
		String post_data = Utility.convertDTO2JsonString(walletTransferRequestDto).toString();

		String checksum = generatePaytmCheckSum(post_data);
		
		
		try {
			HttpResponse<WalletTransferRes> walletTransferRes = Unirest.post(walletBaseTransferURL).header(CONTENTTYPE, APPTYPE)
					.header("x-mid", walletMid).header("x-checksum", checksum).body(walletTransferRequestDto)
					.asObject(WalletTransferRes.class).ifFailure(VanCreationResError.class, r -> {
						VanCreationResError e = r.getBody();
					});
				
			return walletTransferRes.getBody();
		}catch(Exception e) {
			logger.error(e.getMessage());
			throw new ValidationExceptions(PAYTM_SYSTEM_EXCEPTION_ES1001, FormValidationExceptionEnums.ES1001);
		}
		
	}

	public WalletTransferRes doAccountTransfer(AccountTransferMerReq dto, String merchantid)
			throws ValidationExceptions, JsonProcessingException {
		
		MerchantWalletDetails Guuidno = merchantWalletDetailsRepo.findByMerchantId(merchantid);
		if(Guuidno == null) {
			throw new ValidationExceptions(ACCOUNT_NOT_FOUND_E0102, FormValidationExceptionEnums.BANK_ERROR);
		}
		
		PayTmAccountRequestsDto payTmAccountRequestsDto = new PayTmAccountRequestsDto();
		payTmAccountRequestsDto.setOrderId(dto.getInternalOrderid());
		payTmAccountRequestsDto.setSubwalletGuid(Guuidno.getWalletGuuid());
		payTmAccountRequestsDto.setAmount(String.valueOf(dto.getAmount()));
		payTmAccountRequestsDto.setBeneficiaryAccount(dto.getBankaccount());
		payTmAccountRequestsDto.setBeneficiaryName(dto.getBeneficiaryName());
		payTmAccountRequestsDto.setBeneficiaryIFSC(dto.getIfsc());
		payTmAccountRequestsDto.setPurpose(dto.getPurpose());
		payTmAccountRequestsDto.setTransferMode(dto.getRequestType());
		String date = java.time.LocalDate.now().toString();
		payTmAccountRequestsDto.setDate(date);
		
		logger.info("Payload for Account Transfer Request to Paytm :: "+Utility.convertDTO2JsonString(payTmAccountRequestsDto));
		String post_data = Utility.convertDTO2JsonString(payTmAccountRequestsDto).toString();

		String checksum = generatePaytmCheckSum(post_data);
		
		
		HttpResponse<WalletTransferRes> walletTransferRes = Unirest.post(bankAccountTransferBaseUrl).header(CONTENTTYPE, APPTYPE)
				.header("x-mid", walletMid).header("x-checksum", checksum).body(payTmAccountRequestsDto)
				.asObject(WalletTransferRes.class).ifFailure(VanCreationResError.class, r -> {
					VanCreationResError e = r.getBody();
				});
		
		logger.info("Response from Paytm :: "+walletTransferRes.getBody());

		return walletTransferRes.getBody();
	}
	
	public WalletTransferRes doAccountTransferUPI(AccountTransferUPIMerReq dto, String merchantid)
			throws ValidationExceptions, JsonProcessingException {
		
		MerchantWalletDetails Guuidno = merchantWalletDetailsRepo.findByMerchantId(merchantid);
		if(Guuidno == null) {
			throw new ValidationExceptions(ACCOUNT_NOT_FOUND_E0102, FormValidationExceptionEnums.BANK_ERROR);
		}
		
		PayTmAccountRequestsDto payTmAccountRequestsDto = new PayTmAccountRequestsDto();
		payTmAccountRequestsDto.setOrderId(dto.getInternalOrderid());
		payTmAccountRequestsDto.setSubwalletGuid(Guuidno.getWalletGuuid());
		payTmAccountRequestsDto.setAmount(String.valueOf(dto.getAmount()));
		payTmAccountRequestsDto.setBeneficiaryVPA(dto.getBeneficiaryVPA());
		payTmAccountRequestsDto.setPurpose(dto.getPurpose());
		payTmAccountRequestsDto.setTransferMode(dto.getRequestType());
		String date = java.time.LocalDate.now().toString();
		payTmAccountRequestsDto.setDate(date);
		
		logger.info("Payload for Account Transfer Request to Paytm :: "+Utility.convertDTO2JsonString(payTmAccountRequestsDto));
		String post_data = Utility.convertDTO2JsonString(payTmAccountRequestsDto).toString();

		String checksum = generatePaytmCheckSum(post_data);		
		
		HttpResponse<WalletTransferRes> walletTransferRes = Unirest.post(bankAccountTransferBaseUrl).header(CONTENTTYPE, APPTYPE)
				.header("x-mid", walletMid).header("x-checksum", checksum).body(payTmAccountRequestsDto)
				.asObject(WalletTransferRes.class).ifFailure(VanCreationResError.class, r -> {
					VanCreationResError e = r.getBody();
				});
		
		logger.info("Response from Paytm :: "+walletTransferRes.getBody());

		return walletTransferRes.getBody();
	}
	
	public TransactionResponseMerRes doWalletTransferStatus(TransferStatusReq dto, String merchantid)
			throws JsonProcessingException, ValidationExceptions {
		
		MerchantWalletDetails Guuidno = merchantWalletDetailsRepo.findByMerchantId(merchantid);
		if(Guuidno == null) {
			throw new ValidationExceptions(ACCOUNT_NOT_FOUND_E0102, FormValidationExceptionEnums.BANK_ERROR);
		}
		logger.info("Response from Paytm Status order id:: "+Utility.convertDTO2JsonString(dto).toString());
		TransactionDetails transactionDetails = transactionDetailsRepo.findByOrderId(dto.getOrderId());
		logger.info("Response from Paytm Status order id:: "+Utility.convertDTO2JsonString(transactionDetails).toString());
		PaytmTransferStatusRequestDto transferStatusRequestDto = new PaytmTransferStatusRequestDto(transactionDetails.getInternalOrderId());
		String post_data = Utility.convertDTO2JsonString(transferStatusRequestDto).toString();
		logger.info("Response from Paytm Status:: "+post_data);
		String checksum = generatePaytmCheckSum(post_data);
		
		TransferStatusRes transferStatusRes = Unirest
				.post(transferStatusURL)
				.header(CONTENTTYPE, APPTYPE).header("x-mid", walletMid).header("x-checksum", checksum)
				.body(transferStatusRequestDto).asObject(TransferStatusRes.class)
				.ifFailure(VanCreationResError.class, r -> {
					VanCreationResError e = r.getBody();
				}).getBody();
		System.out.println(Utility.convertDTO2JsonString(transferStatusRes));
		
		TransactionResponseMerRes transactionResponseMerRes = new TransactionResponseMerRes();
		if (!transferStatusRes.getStatus().equals(PaytmWalletConstants.FAILURE)) {
			transactionResponseMerRes.setOrderid(transferStatusRes.getResult().getOrderId());
			transactionResponseMerRes.setAmount(transferStatusRes.getResult().getAmount());
		}
		transactionResponseMerRes.setOrderid(dto.getOrderId());
		transactionResponseMerRes.setMessage(transferStatusRes.getStatusMessage());
		transactionResponseMerRes.setStatus(transferStatusRes.getStatus());
		return transactionResponseMerRes;

	}

	public PaytmAccountVerifyResponse verifyBankAccount(BankAccountVerifyRequest dto) throws ValidationExceptions, JsonProcessingException {
		
		MerchantWalletDetails Guuidno = merchantWalletDetailsRepo.findByMerchantId(dto.getMerchantId());
		if(Guuidno == null) {
			throw new ValidationExceptions(ACCOUNT_NOT_FOUND_E0102, FormValidationExceptionEnums.BANK_ERROR);
		}
		
		PaytmAccountVerifyRequest paytmAccountVerifyRequest =new PaytmAccountVerifyRequest();
		paytmAccountVerifyRequest.setBeneficiaryAccount(dto.getBeneficiaryAccountNo());
		paytmAccountVerifyRequest.setBeneficiaryIFSC(dto.getBeneficiaryIFSCCode());
		paytmAccountVerifyRequest.setOrderId(dto.getInternalOrderId());
		paytmAccountVerifyRequest.setSubwalletGuid(Guuidno.getWalletGuuid());
		paytmAccountVerifyRequest.setCallbackUrl(accountVerifyCallBackApi);
		paytmAccountVerifyRequest.setBeneficiaryVPA(dto.getBeneficiaryVPA());
		
		String post_data = Utility.convertDTO2JsonString(paytmAccountVerifyRequest).toString();
		String checksum = generatePaytmCheckSum(post_data);
		
		logger.info("Input payload :: "+post_data);
		
		PaytmAccountVerifyResponse paytmAccountVerifyResponse = Unirest
				.post(accountVerify)
				.header(CONTENTTYPE, APPTYPE).header("x-mid", walletMid).header("x-checksum", checksum)
				.body(paytmAccountVerifyRequest).asObject(PaytmAccountVerifyResponse.class)
				.ifFailure(VanCreationResError.class, r -> {
					VanCreationResError e = r.getBody();
				}).getBody();
		
		return paytmAccountVerifyResponse;
	}
	
	public BalanceCheckMerRes getBalance(String merchantid) throws JsonProcessingException, ValidationExceptions {
		
		MerchantWalletDetails Guuidno = merchantWalletDetailsRepo.findByMerchantId(merchantid);
		logger.info(Utility.convertDTO2JsonString(Guuidno));
		if(Guuidno == null) {
			throw new ValidationExceptions(ACCOUNT_NOT_FOUND_E0102, FormValidationExceptionEnums.BANK_ERROR);
		}
				
		PayTmBalanceCheckReq payTmBalanceCheckReq = new PayTmBalanceCheckReq();
		payTmBalanceCheckReq.setSubwalletGuid(Guuidno.getWalletGuuid());

		String post_data = Utility.convertDTO2JsonString(payTmBalanceCheckReq).toString();		
		String checksum = generatePaytmCheckSum(post_data);		
		logger.info(Utility.convertDTO2JsonString(payTmBalanceCheckReq));
		PayTmBalanceCheckRes payTmBalanceCheckRes = Unirest
				.post(getWalletBalance).header(CONTENTTYPE, APPTYPE)
				.header("x-mid", walletMid).header("x-checksum", checksum).body(payTmBalanceCheckReq)
				.asObject(PayTmBalanceCheckRes.class).ifFailure(VanCreationResError.class, r -> {
					VanCreationResError e = r.getBody();
				}).getBody();
		logger.info(Utility.convertDTO2JsonString(payTmBalanceCheckRes));
		BalanceCheckMerRes balanceCheckMerRes = new BalanceCheckMerRes();
		if (payTmBalanceCheckRes.getStatus().equals(PaytmWalletConstants.SUCCESS)) {
			PayTmBalanceCheckResults payTmBalanceCheckResults = payTmBalanceCheckRes.getResult().get(0);
			balanceCheckMerRes.setLastUpdatedDate(payTmBalanceCheckResults.getLastUpdatedDate());
			balanceCheckMerRes.setWalletBalance(payTmBalanceCheckResults.getWalletBalance());
		}
		balanceCheckMerRes.setMerchantId(merchantid);
		balanceCheckMerRes.setMessage(payTmBalanceCheckRes.getStatusMessage());
		balanceCheckMerRes.setStatus(payTmBalanceCheckRes.getStatus());

		return balanceCheckMerRes;
	}

	public TransactionReportMerRes getTransactionReport(
			TransactionReportMerReq dto, String merchantId)
			throws JsonProcessingException, ValidationExceptions {
		
		MerchantWalletDetails Guuidno = merchantWalletDetailsRepo.findByMerchantId(merchantId);
		if(Guuidno == null) {
			throw new ValidationExceptions(ACCOUNT_NOT_FOUND_E0102, FormValidationExceptionEnums.BANK_ERROR);
		}		
		
		PayTmTransactionReportReq payTmTransactionReportReq = new PayTmTransactionReportReq();
		payTmTransactionReportReq.setSubwalletGuid(Guuidno.getWalletGuuid());
		payTmTransactionReportReq.setFromDate(dto.getFromDate());
		payTmTransactionReportReq.setToDate(dto.getToDate());
		payTmTransactionReportReq.setPageSize(10000);

		String post_data = Utility.convertDTO2JsonString(payTmTransactionReportReq).toString();
		
		String checksum = generatePaytmCheckSum(post_data);		

		PayTmTransactionReportRes payTmTransactionReportRes = Unirest
				.post(transactionReportWallet).header(CONTENTTYPE, APPTYPE)
				.header("x-mid", walletMid).header("x-checksum", checksum).body(payTmTransactionReportReq)
				.asObject(PayTmTransactionReportRes.class).ifFailure(VanCreationResError.class, r -> {
					VanCreationResError e = r.getBody();
				}).getBody();
		
		TransactionReportMerRes transactionReportMerRes = new TransactionReportMerRes();
		transactionReportMerRes.setStatus(payTmTransactionReportRes.getStatus());
		transactionReportMerRes.setStatusMessage(payTmTransactionReportRes.getStatusMessage());
		
		if(payTmTransactionReportRes.getStatus().equals(PaytmWalletConstants.SUCCESS)) {
			
			List<TransactionReportMerReqResult> transactionReportMerReqResults = new ArrayList<TransactionReportMerReqResult>();
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

	
	
	/*

	public TransferFundsRes doTransferFunds(TransferFundsReq dto) throws Exception {

		PaytmTransferFundsRequestDto transferFundsRequestDto = new PaytmTransferFundsRequestDto(dto.getAmount(), GUUID

		);
		String post_data = transferFundsRequestDto.toString();
		String checksum = PaytmChecksum.generateSignature(post_data, merchantKEYUAT);

		HttpResponse<TransferFundsRes> transFundRes = Unirest.post(WalletBaseURL).header(CONTENTTYPE, APPTYPE)
				.header("x-mid", x_midUAT).header("x-checksum", checksum).body(transferFundsRequestDto)
				.asObject(TransferFundsRes.class).ifFailure(VanCreationResError.class, r -> {
					VanCreationResError e = r.getBody();

				});

		return transFundRes.getBody();

	}*/
}
