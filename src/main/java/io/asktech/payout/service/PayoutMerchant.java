package io.asktech.payout.service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;

import ch.qos.logback.classic.pattern.Util;
import io.asktech.payout.constants.ErrorValues;
import io.asktech.payout.constants.PaytmWalletConstants;
import io.asktech.payout.dto.admin.UpdateTransactionDetailsRequestDto;
import io.asktech.payout.dto.merchant.AccountTransferMerReq;
import io.asktech.payout.dto.merchant.AccountTransferMerRes;
import io.asktech.payout.dto.merchant.AccountTransferUPIMerReq;
import io.asktech.payout.dto.merchant.BalanceCheckMerRes;
import io.asktech.payout.dto.merchant.BankAccountVerfiyResponse;
import io.asktech.payout.dto.merchant.BankAccountVerifyRequest;
import io.asktech.payout.dto.merchant.TransactionFilterReq;
import io.asktech.payout.dto.merchant.TransactionReportMerReq;
import io.asktech.payout.dto.merchant.TransactionReportMerRes;
import io.asktech.payout.dto.merchant.TransactionRequestFilterMerReq;
import io.asktech.payout.dto.merchant.TransactionResponseMerRes;
import io.asktech.payout.dto.merchant.WalletFilterReq;
import io.asktech.payout.dto.merchant.WalletTransferMerReq;
import io.asktech.payout.dto.merchant.WalletTransferMerRes;
import io.asktech.payout.dto.reqres.TransferStatusReq;
import io.asktech.payout.dto.reqres.WalletTransferRes;
import io.asktech.payout.dto.utility.SuccessResponseDto;
import io.asktech.payout.enums.FormValidationExceptionEnums;
import io.asktech.payout.enums.SuccessCode;
import io.asktech.payout.enums.TransactionStatus;
import io.asktech.payout.enums.TransactionType;
import io.asktech.payout.enums.WalletStatus;
import io.asktech.payout.enums.WalletType;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.modal.merchant.AccountTransactionReq;
import io.asktech.payout.modal.merchant.AccountTransactionUPIReq;
import io.asktech.payout.modal.merchant.BankAccountVerifyReqDetails;
import io.asktech.payout.modal.merchant.MerchantPgConfig;
import io.asktech.payout.modal.merchant.PgDetails;
import io.asktech.payout.modal.merchant.TransactionDetails;
import io.asktech.payout.modal.merchant.WalletTransactionReq;
import io.asktech.payout.paytm.requests.dto.PaytmAccountVerifyResponse;
import io.asktech.payout.paytm.requests.dto.TransactionDetailsDto;
import io.asktech.payout.repository.merchant.AccountTransactionReqRepo;
import io.asktech.payout.repository.merchant.AccountTransactionUPIReqRepo;
import io.asktech.payout.repository.merchant.BankAccountVerifyReqDetailsRepo;
import io.asktech.payout.repository.merchant.MerchantPgConfigRepo;
import io.asktech.payout.repository.merchant.TransactionDetailsRepo;
import io.asktech.payout.repository.merchant.WalletTransactionReqRepo;
import io.asktech.payout.service.cashfree.requests.CashfreeAsyncStatus;
import io.asktech.payout.service.paytm.requests.PaytmWalletRequests;
import io.asktech.payout.service.save.MerchantRequestSave;
import io.asktech.payout.service.validations.PayoutMerchantReqValidation;
import io.asktech.payout.utils.Utility;
import io.asktech.payout.utils.Validator;
import io.asktech.payout.utils.RandomNameGenerator.RandomName;
import io.asktech.payout.wallet.dto.WalletRechargeReqDto;
import io.asktech.payout.wallet.dto.WalletRechargeResDto;
import io.asktech.payout.wallet.dto.WalletReversalReqDto;
import io.asktech.payout.wallet.modal.BalanceProcessorQueue;
import io.asktech.payout.wallet.modal.CommissionsDeduction;
import io.asktech.payout.wallet.modal.WalletDetails;
import io.asktech.payout.wallet.modal.WalletTransactions;
import io.asktech.payout.wallet.repository.BalanceProcessorQueueRepository;
import io.asktech.payout.wallet.repository.CommissionsDeductionRepo;
import io.asktech.payout.wallet.repository.WalletTransactionsRepo;
import io.asktech.payout.wallet.service.WalletService;

@Service
public class PayoutMerchant implements ErrorValues {

	private static final String PG_SERVICE_NOT_MAPPED = "PG Service Not mapped. Please Contact admin!";
	static Logger logger = LoggerFactory.getLogger(PayoutMerchant.class);

	@Autowired
	PayoutMerchantReqValidation payoutMerchantReqValidation;
	@Autowired
	MerchantRequestSave merchantRequestSave;
	@Autowired
	PaytmWalletRequests paytmWalletRequests;
	@Autowired
	PayoutWalletAdminService payoutWalletAdminService;

	@Autowired
	WalletTransactionReqRepo walletTransactionReqRepo;

	@Autowired
	AccountTransactionReqRepo accountTransactionReqRepo;
	@Autowired
	BankAccountVerifyReqDetailsRepo bankAccountVerifyReqDetailsRepo;
	@Autowired
	TransactionDetailsRepo transactionDetailsRepository;
	@Autowired
	AccountTransactionUPIReqRepo accountTransactionUPIReqRepo;
	@Autowired
	WalletService walletService;
	@Autowired
	BalanceProcessorQueueRepository balanceProcessorQueueRepository;

	// @Value("${PayoutTransfers.Account}")
	// String accountTransfer;
	// @Value("${PayoutTransfers.Upi}")
	// String upiTransfer;
	// @Value("${PayoutTransfers.Wallet}")
	// String walletTransfer;

	@Autowired
	CashfreeAsyncStatus cashfreeAsyncStatus;

	// ------------------TRANSFERS -----------------------------

	public WalletTransferMerRes walletTransfer(WalletTransferMerReq dto, String merchantId)
			throws JsonProcessingException, ValidationExceptions, ParseException {

		logger.info("Inside Service WalletTransfer() ");

		List<WalletTransactionReq> walletTransactionReq = walletTransactionReqRepo
				.findByMerchantIdAndOrderId(merchantId, dto.getOrderid());
		if (walletTransactionReq.size() > 0) {
			logger.error("Duplicate request come from Merchant , OrderId already exits in System.");
			throw new ValidationExceptions(WALLET_ORDERID_EXITS_E0104, FormValidationExceptionEnums.E0104);
		}
		String status = "FAILED";
		String message = "Unable to Process";
		WalletTransferMerRes walletTransferMerRes = new WalletTransferMerRes();
		walletTransferMerRes.setOrderid(dto.getOrderid());
		dto.setInternalOrderId(Utility.getRandomId());
		PgDetails merchantPgConfig = payoutUtils.getMerchantPgDetails(merchantId, "WALLET");
		if (merchantPgConfig == null) {
			throw new ValidationExceptions(PG_SERVICE_NOT_MAPPED, FormValidationExceptionEnums.E0100);
		}
		dto.setPgName(merchantPgConfig.getPgName());
		dto.setPgId(merchantPgConfig.getPgId());
		if (!balanceCheck(merchantId, dto.getAmount())) {
			throw new ValidationExceptions(INSUFICIENT_BALANCE_E0100, FormValidationExceptionEnums.E0100);
		}
		if (payoutMerchantReqValidation.WalletTransferValidation(dto)) {
			logger.info("Request Validation completed... ");
			if (merchantRequestSave.MerchantWalletTransferRequest(dto, merchantId)) {
				logger.info("Persists Request into Database .. ");
				WalletRechargeResDto py = balanceDeduction(dto, merchantId);
				if (py.getStatus().equals("SUCCESS")) {
					WalletTransferRes wallettransferRes = null;
					try {
						wallettransferRes = payoutWalletAdminService.getWalletTransfer(dto, merchantId);
						if (!wallettransferRes.getStatus().equalsIgnoreCase(PaytmWalletConstants.FAILURE)) {
							merchantRequestSave.MerchantTransactionDetails(dto, wallettransferRes, merchantId);
						}
						status = wallettransferRes.getStatus();
						message = wallettransferRes.getStatusMessage();
					} catch (Exception e) {
						status = PaytmWalletConstants.FAILURE;
						message = e.getMessage();
					}
					merchantRequestSave.MerchantWalletTransferResponse(wallettransferRes, dto.getOrderid(), merchantId,
							PaytmWalletConstants.WALLET, dto.getInternalOrderId());
				} else {
					throw new ValidationExceptions(INSUFICIENT_BALANCE_E0100, FormValidationExceptionEnums.E0100);
				}
			}

		}
		walletTransferMerRes.setMessage(message);
		walletTransferMerRes.setStatus(status);
		if (status.equalsIgnoreCase(PaytmWalletConstants.FAILURE)) {
			balanceRefund(dto, merchantId);
		}
		return walletTransferMerRes;

	}

	@Autowired
	PayoutUtils payoutUtils;

	public AccountTransferMerRes accountTransfer(AccountTransferMerReq dto, String merchantId)
			throws ValidationExceptions, JsonProcessingException, ParseException {
		String status = "ACCEPTED";
		String message = "Request Accepted";
		AccountTransferMerRes accountTransferMerRes = new AccountTransferMerRes();
		accountTransferMerRes.setOrderid(dto.getOrderid());
		dto.setInternalOrderid(Utility.getRandomId());

		List<AccountTransactionReq> accountTransferMerReq = accountTransactionReqRepo
				.findByMerchantIdAndOrderId(merchantId, dto.getOrderid());
		if (accountTransferMerReq.size() > 0) {
			logger.error("Duplicate request come from Merchant , OrderId already exits in System.");
			throw new ValidationExceptions(ACCOUNT_ORDERID_EXITS_E0106, FormValidationExceptionEnums.E0106);
		}
		
		PgDetails merchantPgConfig = payoutUtils.getMerchantPgDetails(merchantId, dto.getRequestType());
		if (merchantPgConfig == null) {
			throw new ValidationExceptions(PG_SERVICE_NOT_MAPPED, FormValidationExceptionEnums.E0100);
		}

		if (!balanceCheck(merchantId,
				commDeductedAmt(dto.getAmount(), dto.getRequestType(), merchantPgConfig.getPgName()))) {
			throw new ValidationExceptions(INSUFICIENT_BALANCE_E0100, FormValidationExceptionEnums.E0100);
		}
		logger.info("Account Balance Check Complete");

		dto.setPgName(merchantPgConfig.getPgName());
		dto.setReqStatus("STARTED");
		dto.setPgid(merchantPgConfig.getPgId());
		dto.setBeneficiaryName(getBeneficiaryName(merchantId, dto.getBeneficiaryName(), dto.getRequestType()));
		if (payoutMerchantReqValidation.accountTransferValidation(dto)) {
			if (merchantRequestSave.MerchantAccountTransferRequest(dto, merchantId)) {
				logger.info("Account Transfer Balance Deduction Start");
				// Wallet Deduction

				dto.setReqStatus("ACCEPTED");
				merchantRequestSave.MerchantTransactionDetails(dto, merchantId);
				status = "ACCEPTED";
				message = "Request Accepted";
			} else {
				status = "FAILURE";
				message = "Request Failed";
			}
		} else {
			status = "FAILURE";
			message = "Request Failed";
		}
		// Wallet Refund in Failure
		accountTransferMerRes.setMessage(message);
		accountTransferMerRes.setStatus(status);

		return accountTransferMerRes;

	}

	public AccountTransferMerRes accountTransferUPI(AccountTransferUPIMerReq dto, String merchantId)
			throws ParseException, ValidationExceptions, JsonProcessingException {
		String status = "FAILED";
		String message = "Unable to Process";
		logger.info("accountTransferUPI Service");
		logger.info("AccountTransferUPIMerReq:: " + Utility.convertDTO2JsonString(dto));
		AccountTransferMerRes accountTransferMerRes = new AccountTransferMerRes();
		accountTransferMerRes.setOrderid(dto.getOrderid());
		dto.setInternalOrderid(Utility.getRandomId());
		dto.setBeneficiaryName(getBeneficiaryName(merchantId, dto.getBeneficiaryName(), "UPI"));
		logger.info("accountTransferUPI Order Assignment");

		List<AccountTransactionUPIReq> accountTransferMerReq = accountTransactionUPIReqRepo
				.findByMerchantIdAndOrderId(merchantId, dto.getOrderid());
		logger.info(Utility.convertDTO2JsonString(accountTransferMerReq));
		if (accountTransferMerReq.size() > 0) {
			logger.error("Duplicate request come from Merchant , OrderId already exits in System.");
			throw new ValidationExceptions(ACCOUNT_ORDERID_EXITS_E0106, FormValidationExceptionEnums.E0106);
		}
		logger.info("accountTransferUPI ORDER ID Check");
		PgDetails merchantPgConfig = payoutUtils.getMerchantPgDetails(merchantId, dto.getRequestType());
		if (merchantPgConfig == null) {
			throw new ValidationExceptions(PG_SERVICE_NOT_MAPPED, FormValidationExceptionEnums.E0100);
		}
		if (!balanceCheck(merchantId, commDeductedAmt(dto.getAmount(), "UPI", merchantPgConfig.getPgName()))) {
			throw new ValidationExceptions(INSUFICIENT_BALANCE_E0100, FormValidationExceptionEnums.E0100);
		}

		dto.setPgName(merchantPgConfig.getPgName());
		dto.setPgId(merchantPgConfig.getPgId());
		dto.setReqStatus("STARTED");
		logger.info("Validation Complete accountTransferUPI Service");
		if (payoutMerchantReqValidation.accountTransferValidation(dto)) {
			logger.info("Validation Complete accountTransferValidation ");

			if (merchantRequestSave.MerchantAccountTransferRequest(dto, merchantId)) {
				dto.setReqStatus("ACCEPTED");
				merchantRequestSave.merchantTransactionDetailsUPI(dto, merchantId);
				status = "ACCEPTED";
				message = "Request Accepted";
			}
		}

		accountTransferMerRes.setMessage(message);
		accountTransferMerRes.setStatus(status);

		return accountTransferMerRes;
	}

	@Autowired
	MerchantPgConfigRepo merchantPgConfigRepo;
	@Autowired
	RandomName randomName;

	public String getBeneficiaryName(String merchantId, String beneName, String service)
			throws JsonProcessingException {
		MerchantPgConfig merchantPgConfig = merchantPgConfigRepo.findByMerchantIdAndServiceAndStatus(merchantId,
				service, "ACTIVE");
		logger.info("getBeneficiaryName::" + Utility.convertDTO2JsonString(merchantPgConfig));
		if (merchantPgConfig.getEnableRandomName() != null) {
			if (merchantPgConfig.getEnableRandomName().equalsIgnoreCase("TRUE")) {
				logger.info("getEnableRandomName::TRUE");
				return randomName.getRandomName();
			}
		}
		beneName = Validator.removeNumber(beneName);
		if (beneName.length() < 3) {
			logger.info("getEnableRandomName::beneLength");
			return randomName.getRandomName();
		}
		return beneName;
	}

	// -------------------------- REPORTING --------------------------------
	public TransactionReportMerRes transactionReport(TransactionReportMerReq dto, String merchantid)
			throws JsonProcessingException, ValidationExceptions {
		return payoutWalletAdminService.getTransactionReport(dto, merchantid);
	}

	public TransactionResponseMerRes transactionStatus(TransferStatusReq dto, String merchantid)
			throws JsonProcessingException, ValidationExceptions {
		TransactionResponseMerRes trres = payoutWalletAdminService.getWalletTransferStatus(dto, merchantid);
		if (trres.getStatus() != null) {
			if (!trres.getStatus().equalsIgnoreCase("ERROR")) {
				cashfreeAsyncStatus.updateStatus(dto.getOrderId(), trres);
			}
		}
		return trres;

	}

	public BankAccountVerfiyResponse bankAccountVerify(BankAccountVerifyRequest dto)
			throws ValidationExceptions, JsonProcessingException, ParseException {

		logger.info("Inside bankAccountVerify() " + Utility.convertDTO2JsonString(dto));
		String status = "FAILED";
		String message = "Unable to Process";
		BankAccountVerfiyResponse bankAccountVerfiyResponse = new BankAccountVerfiyResponse();

		BankAccountVerifyReqDetails bankAccountVerifyReqDetails = bankAccountVerifyReqDetailsRepo
				.findByOrderId(dto.getOrderId());
		if (bankAccountVerifyReqDetails != null) {
			logger.error("Duplicate request come from Merchant , OrderId already exits in System.");
			throw new ValidationExceptions(WALLET_ORDERID_EXITS_E0104, FormValidationExceptionEnums.E0104);
		}

		logger.info("After Checking the duplicate Request." + Utility.convertDTO2JsonString(dto));
		dto.setInternalOrderId(Utility.getRandomId());

		if (payoutMerchantReqValidation.bankAccountVerfiyValidation(dto)) {
			logger.info("bankAccountVerfiyValidation done::");
			if (merchantRequestSave.bankAccountVerifyReqDetails(dto)) {
				logger.info("bankAccountVerifyReqDetails after Save done::");
				PaytmAccountVerifyResponse paytmAccountVerifyResponse = payoutWalletAdminService.verifyBankAccount(dto);
				logger.info("verify Account Response from Paytm :: "
						+ Utility.convertDTO2JsonString(paytmAccountVerifyResponse));

				status = paytmAccountVerifyResponse.getStatus();
				message = paytmAccountVerifyResponse.getStatusMessage();
				merchantRequestSave.bankAccountVerifyResDetails(dto, paytmAccountVerifyResponse);

			}
		}
		bankAccountVerfiyResponse.setOrderId(dto.getOrderId());
		bankAccountVerfiyResponse.setStatus(status);
		bankAccountVerfiyResponse.setMessage(message);
		return bankAccountVerfiyResponse;

	}

	@Autowired
	WalletTransactionsRepo walletTransactionsRepo;

	public Object walletReport(TransactionReportMerReq dto, String merchantid)
			throws ParseException, ValidationExceptions {
		String fromDate = "";
		String toDate = "";

		if (txnParam(dto.getFromDate()) == false || txnParam(dto.getToDate()) == false) {
			throw new ValidationExceptions(WALLET_REPORT_FROM_DATE_E114, FormValidationExceptionEnums.E114);
		}

		if (txnParam(dto.getFromDate()) == true) {
			fromDate = Utility.convertDatetoMySqlDateFormat(dto.getFromDate());
		}

		if (txnParam(dto.getToDate()) == true) {
			toDate = Utility.convertDatetoMySqlDateFormat(dto.getToDate());
		}

		// if(txnParam(dto.getTransactionType())==true) {
		// if (!Validator.containsEnum(WalletType.class, dto.getTransactionType())) {
		// throw new ValidationExceptions(WALLET_TYPE,
		// FormValidationExceptionEnums.INPUT_VALIDATION_ERROR);
		// }}

		// if(txnParam(fromDate)==true && txnParam(toDate)==false &&
		// txnParam(dto.getTransactionType())==true) {
		// return walletTransactionsRepo.getByDateFromAndTransactionType(merchantid,
		// fromDate, dto.getTransactionType());
		// }

		if (txnParam(fromDate) == true && txnParam(toDate) == true && txnParam(dto.getTransactionType()) == true) {
			return walletTransactionsRepo.getByDateAndTransactionTypePay(merchantid, fromDate, toDate,
					dto.getTransactionType());
		}

		if (txnParam(dto.getTransactionType()) == true) {
			return walletTransactionsRepo.getSearchByMerchantIdAndTransactionType(dto.getTransactionType(), merchantid);
		}
		//
		// if (txnParam(toDate)==false && txnParam(dto.getTransactionType())==false) {
		// return walletTransactionsRepo.getByCreatedAndMerchantId(fromDate,
		// merchantid);
		// }
		//
		if (txnParam(fromDate) == true && txnParam(toDate) == true && txnParam(dto.getTransactionType()) == false)
			return walletTransactionsRepo.getByCreatedAndMerchantBetween(fromDate, toDate, merchantid);

		return null;

	}

	// public List<WalletTransactions> wallet7DaysReport(String merchantid)
	// throws ParseException, ValidationExceptions {
	// return walletTransactionsRepo.findLast7DaysTransaction(merchantid);
	// }

	public Object transactionReportwithFilter(TransactionRequestFilterMerReq dto, String merchantid)
			throws JsonProcessingException, ValidationExceptions, ParseException {
		logger.info("Inside getMerchantTransactionFilterWise merchant Id :: " + merchantid);
		String status = null;
		String orderId = null;
		String dateTo = null;
		String dateFrom = null;
		String TransactionType = null;
		if (dto.getOrderId() != null) {
			orderId = dto.getOrderId().replace("%", "");
			logger.info("Order ID Search");
		} else {
			orderId = "%";
		}

		List<TransactionDetails> listTransactionDetals = new ArrayList<>();
		if ((orderId != null) && (orderId.length() > 2)) {
			logger.info("By OrderID:: " + orderId + " merchant Id :: " + merchantid);
			listTransactionDetals = transactionDetailsRepository.findByOrderIdAndMerchantId(orderId, merchantid);
		} else {
			if (txnParam(dto.getFromDate()) == true) {
				dateFrom = dto.getFromDate().replace("%", "");
				if (!Validator.isValidateDateFormat(dateFrom)) {
					logger.info("Date validation error ... " + dateFrom);
					throw new ValidationExceptions(DATE_FORMAT_VALIDATION,
							FormValidationExceptionEnums.DATE_FORMAT_VALIDATION);
				}
				dateFrom = Utility.convertDatetoMySqlDateFormat(dateFrom);
				logger.info("From Date Search");
			} else {
				dateFrom = "%";
				logger.info("From Date Null");
			}
			if (txnParam(dto.getToDate()) == true) {
				dateTo = dto.getToDate().replace("%", "");
				if (!Validator.isValidateDateFormat(dateTo)) {
					logger.info("Date validation error ... " + dateTo);
					throw new ValidationExceptions(DATE_FORMAT_VALIDATION,
							FormValidationExceptionEnums.DATE_FORMAT_VALIDATION);
				}
				dateTo = Utility.convertDatetoMySqlDateFormat(dateTo);
				logger.info("To Date::" + dateTo);
			} else {
				if (dateFrom.equals("%")) {
					DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					LocalDate pastMonthDate = LocalDate.now().minusMonths(1);
					dateFrom = pastMonthDate.toString();
					String dateToday = LocalDate.now().format(dateTimeFormatter);
					dateTo = dateToday;
					logger.info("Dates::" + dateFrom + "::" + dateTo);
				} else {
					DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					LocalDate frdate = LocalDate.parse(dateFrom, dateTimeFormatter);
					dateTo = frdate.plusDays(1).toString();
					logger.info("1 day Dates::" + dateFrom + "::" + dateTo);
				}

			}

			logger.info("Final day Dates::" + dateFrom + "::" + dateTo);
			if (txnParam(dto.getTransactionType()) == true) {
				TransactionType = dto.getTransactionType().replace("%", "");
			} else {
				TransactionType = "%";
			}
			if (txnParam(dto.getStatus()) == true) {
				status = dto.getStatus().replace("%", "");
			} else {
				status = "%";
			}
			logger.info(
					"DATA::" + status + "::" + TransactionType + "::" + dateFrom + "::" + dateTo + "::" + merchantid);
			listTransactionDetals = transactionDetailsRepository.getSearchByDetails(status, TransactionType, dateFrom,
					dateTo, merchantid);
		}

		List<TransactionDetailsDto> trdetails = new ArrayList<>();
		for (TransactionDetails tr : listTransactionDetals) {
			TransactionDetailsDto trd = new TransactionDetailsDto();
			trd.setUtrId(tr.getUtrid());
			trd.setAmount(tr.getAmount());
			trd.setBankaccount(tr.getBankaccount());
			trd.setTrDateTime(tr.getCreated().toString());
			trd.setLastUpdate(tr.getUpdated().toString());
			trd.setBeneficiaryName(tr.getBeneficiaryName());
			trd.setIfsc(tr.getIfsc());
			trd.setMerchantId(tr.getMerchantId());
			trd.setMerchantOrderId(tr.getOrderId());
			trd.setPgOrderId(tr.getPaytmTransactionId());
			trd.setPhonenumber(tr.getPhonenumber());
			trd.setPurpose(tr.getPurpose());
			trd.setTransactionMessage(tr.getTransactionMessage());
			trd.setTransactionStatus(tr.getTransactionStatus());
			trd.setTransactionType(tr.getTransactionType());
			trd.setBeneficiaryVPA(tr.getBeneficiaryVPA());
			trdetails.add(trd);
		}
		return trdetails;
	}

	// --------------Wallet Operations -----------------//

	public boolean balanceCheck(String merchantid, String amount) throws JsonProcessingException, ValidationExceptions {
		boolean t = walletService.checkWalletBalanceExists(merchantid, amount);
		logger.info("Bal Check ok");
		return t;
		// return paytmWalletRequests.getBalance(merchantid);

	}

	public BalanceCheckMerRes balanceCheck(String merchantid) throws ValidationExceptions {
		BalanceCheckMerRes balanceCheckMerRes = new BalanceCheckMerRes();
		WalletDetails walletDetails = walletService.getWalletDetails(merchantid);
		balanceCheckMerRes.setLastUpdatedDate(walletDetails.getUpdated().toString());
		balanceCheckMerRes.setMerchantId(merchantid);
		balanceCheckMerRes.setStatus(walletDetails.getStatus());
		balanceCheckMerRes.setWalletBalance(walletDetails.getAmount());
		balanceCheckMerRes.setMessage("SUCCESS");
		balanceCheckMerRes.setWalletId(walletDetails.getMainWalletid());
		return balanceCheckMerRes;

	}

	@Autowired
	CommissionsDeductionRepo cdr;

	public WalletRechargeResDto balanceDeduction(AccountTransferMerReq dto, String merchantId)
			throws ValidationExceptions, JsonProcessingException {
		logger.info("-------------- ACCOUNT BALANCE DEDUCTION ----------");
		PgDetails merchantPgConfig = payoutUtils.getMerchantPgDetails(merchantId, dto.getRequestType());
		if (merchantPgConfig == null) {
			throw new ValidationExceptions(PG_SERVICE_NOT_MAPPED, FormValidationExceptionEnums.E0100);
		}
		WalletReversalReqDto walletReversalReqDto = new WalletReversalReqDto();
		walletReversalReqDto
				.setAmount(commDeductedAmt(dto.getAmount(), dto.getRequestType(), merchantPgConfig.getPgName()));
		walletReversalReqDto.setReferenceId(dto.getInternalOrderid());
		walletReversalReqDto.setPurpose(dto.getPurpose());
		walletReversalReqDto.setTransactionType(dto.getRequestType());
		walletReversalReqDto.setRemarks(dto.getBankaccount());
		logger.info("balanceDeduction::" + Utility.convertDTO2JsonString(dto));
		WalletRechargeResDto transfer = walletService.walletDeductBalance(walletReversalReqDto, merchantId);
		return transfer;
	}

	public WalletRechargeResDto balanceDeduction(WalletTransferMerReq dto, String merchantId)
			throws ValidationExceptions, JsonProcessingException {
		PgDetails merchantPgConfig = payoutUtils.getMerchantPgDetails(merchantId, "WALLET");
		if (merchantPgConfig == null) {
			throw new ValidationExceptions(PG_SERVICE_NOT_MAPPED, FormValidationExceptionEnums.E0100);
		}
		logger.info("-------------- WALLET BALANCE DEDUCTION ----------");
		WalletReversalReqDto walletReversalReqDto = new WalletReversalReqDto();
		walletReversalReqDto.setAmount(commDeductedAmt(dto.getAmount(), "WALLET", merchantPgConfig.getPgName()));
		walletReversalReqDto.setReferenceId(dto.getInternalOrderId());
		walletReversalReqDto.setPurpose("WALLET TRANSFER");
		walletReversalReqDto.setTransactionType("WALLET");
		walletReversalReqDto.setRemarks(dto.getPhonenumber());
		WalletRechargeResDto transfer = walletService.walletDeductBalance(walletReversalReqDto, merchantId);
		logger.info("Balance Deduction Status ACCOUNT" + transfer.getStatus());
		return transfer;
	}

	public WalletRechargeResDto balanceDeduction(AccountTransferUPIMerReq dto, String merchantId)
			throws ValidationExceptions, JsonProcessingException {
		logger.info("-------------- UPI BALANCE DEDUCTION ----------");
		PgDetails merchantPgConfig = payoutUtils.getMerchantPgDetails(merchantId, dto.getRequestType());
		if (merchantPgConfig == null) {
			throw new ValidationExceptions(PG_SERVICE_NOT_MAPPED, FormValidationExceptionEnums.E0100);
		}
		WalletReversalReqDto walletReversalReqDto = new WalletReversalReqDto();
		walletReversalReqDto.setAmount(commDeductedAmt(dto.getAmount(), "UPI", merchantPgConfig.getPgName()));
		walletReversalReqDto.setReferenceId(dto.getInternalOrderid());
		walletReversalReqDto.setPurpose(dto.getPurpose());
		walletReversalReqDto.setTransactionType("UPI");
		walletReversalReqDto.setRemarks(dto.getBeneficiaryVPA());
		WalletRechargeResDto transfer = walletService.walletDeductBalance(walletReversalReqDto, merchantId);
		logger.info("Balance Deduction Status UPI " + transfer.getStatus());
		return transfer;
	}

	// ---------REFUND ----------------

	public void populateBalanceProcessRefund(AccountTransferMerReq dto, String merchantId) {

		BalanceProcessorQueue balanceProcessorQueue = new BalanceProcessorQueue();
		balanceProcessorQueue.setAmount(dto.getAmount());
		balanceProcessorQueue.setBankaccount(dto.getBankaccount());
		balanceProcessorQueue.setInternalOrderId(dto.getInternalOrderid());
		balanceProcessorQueue.setMerchantId(merchantId);
		balanceProcessorQueue.setMerchantThreadFlag(dto.getMerchantThreadFlag());
		balanceProcessorQueue.setOrderId(dto.getOrderid());
		balanceProcessorQueue.setPhonenumber(dto.getPhonenumber());
		balanceProcessorQueue.setStatus("PENDING");
		balanceProcessorQueue.setTrType("REFUND");
		balanceProcessorQueue.setPgName(dto.getPgName());
		balanceProcessorQueueRepository.save(balanceProcessorQueue);

	}

	public void populateBalanceProcessRefund(AccountTransferUPIMerReq dto, String merchantId) {

		BalanceProcessorQueue balanceProcessorQueue = new BalanceProcessorQueue();
		balanceProcessorQueue.setAmount(dto.getAmount());
		balanceProcessorQueue.setUpiVpa(dto.getBeneficiaryVPA());
		balanceProcessorQueue.setInternalOrderId(dto.getInternalOrderid());
		balanceProcessorQueue.setMerchantId(merchantId);
		balanceProcessorQueue.setMerchantThreadFlag(dto.getMerchantThreadFlag());
		balanceProcessorQueue.setOrderId(dto.getOrderid());
		balanceProcessorQueue.setPhonenumber(dto.getPhonenumber());
		balanceProcessorQueue.setStatus("PENDING");
		balanceProcessorQueue.setTrType("REFUND");
		balanceProcessorQueue.setPgName(dto.getPgName());
		balanceProcessorQueueRepository.save(balanceProcessorQueue);

	}

	public void populateBalanceProcessRefund(TransactionDetails dto, int thread) {
		// List<WalletTransactions> trxs = walletTransactionsRepo
		// .getByReferenceIdandCreditdebit(dto.getInternalOrderId(), "CREDIT");
		// if (trxs.size() == 0) {
		BalanceProcessorQueue balanceProcessorQueue = new BalanceProcessorQueue();
		balanceProcessorQueue.setAmount(dto.getAmount());
		if (dto.getBeneficiaryVPA() != null) {
			balanceProcessorQueue.setUpiVpa(dto.getBeneficiaryVPA());
		}
		if (dto.getBankaccount() != null) {
			balanceProcessorQueue.setBankaccount(dto.getBankaccount());
		}
		balanceProcessorQueue.setInternalOrderId(dto.getInternalOrderId());
		balanceProcessorQueue.setMerchantId(dto.getMerchantId());
		balanceProcessorQueue.setMerchantThreadFlag(thread);
		balanceProcessorQueue.setOrderId(dto.getOrderId());
		balanceProcessorQueue.setPhonenumber(dto.getPhonenumber());
		balanceProcessorQueue.setStatus("PENDING");
		balanceProcessorQueue.setTrType("REFUND");
		balanceProcessorQueue.setPgName(dto.getPgname());
		try {
			balanceProcessorQueueRepository.save(balanceProcessorQueue);
		} catch (Exception e) {
			logger.error(e.getMessage());
			// TODO: handle exception
		}
		// }

	}

	public WalletRechargeResDto balanceRefund(AccountTransferMerReq dto, BalanceProcessorQueue balanceProcessorQueue)
			throws ValidationExceptions, JsonProcessingException {
		WalletRechargeReqDto walletReversalReqDto = new WalletRechargeReqDto();
		/*
		 * TransactionDetails transactionDetails = transactionDetailsRepository
		 * .findByInternalOrderId(dto.getInternalOrderid());
		 * if (transactionDetails != null) {
		 * accountTransfer = transactionDetails.getPgname();
		 * }
		 */
		logger.info("REVERSAL REQ::" + Utility.convertDTO2JsonString(balanceProcessorQueue));
		walletReversalReqDto
				.setAmount(commDeductedAmt(dto.getAmount(), dto.getRequestType(), balanceProcessorQueue.getPgName()));
		walletReversalReqDto.setReferenceId(dto.getInternalOrderid());
		walletReversalReqDto.setPurpose("ACCOUNT REVERSAL");
		walletReversalReqDto.setTransactionType(dto.getRequestType());
		walletReversalReqDto.setRemarks(dto.getBankaccount());
		WalletRechargeResDto transfer = walletService.walletRefundBalance(walletReversalReqDto,
				balanceProcessorQueue.getMerchantId());
		return transfer;
	}

	public WalletRechargeResDto balanceRefund(WalletTransferMerReq dto, String merchantId)
			throws ValidationExceptions, JsonProcessingException {
		WalletRechargeReqDto walletReversalReqDto = new WalletRechargeReqDto();
		TransactionDetails transactionDetails = transactionDetailsRepository
				.findByInternalOrderId(dto.getInternalOrderId());
		String walletTransfer = null;
		if (transactionDetails != null) {
			walletTransfer = transactionDetails.getPgname();
		}
		walletReversalReqDto.setAmount(commDeductedAmt(dto.getAmount(), "WALLET", walletTransfer));
		walletReversalReqDto.setReferenceId(dto.getInternalOrderId());
		walletReversalReqDto.setPurpose("WALLET REVERSAL");
		walletReversalReqDto.setTransactionType("WALLET");
		walletReversalReqDto.setRemarks(dto.getPhonenumber());
		WalletRechargeResDto transfer = walletService.walletRefundBalance(walletReversalReqDto, merchantId);
		return transfer;
	}

	public WalletRechargeResDto balanceRefund(AccountTransferUPIMerReq dto, BalanceProcessorQueue balanceProcessorQueue)
			throws ValidationExceptions, JsonProcessingException {
		WalletRechargeReqDto walletReversalReqDto = new WalletRechargeReqDto();
		/*
		 * TransactionDetails transactionDetails = transactionDetailsRepository
		 * .findByInternalOrderId(dto.getInternalOrderid());
		 * if (transactionDetails != null) {
		 * upiTransfer = transactionDetails.getPgname();
		 * }
		 */
		logger.info("Input for refund initiation :: " + Utility.convertDTO2JsonString(dto));

		walletReversalReqDto.setAmount(commDeductedAmt(dto.getAmount(), "UPI", balanceProcessorQueue.getPgName()));
		walletReversalReqDto.setReferenceId(dto.getInternalOrderid());
		walletReversalReqDto.setPurpose("UPI REVERSAL");
		walletReversalReqDto.setTransactionType("UPI");
		walletReversalReqDto.setRemarks(dto.getBeneficiaryVPA());
		WalletRechargeResDto transfer = walletService.walletRefundBalance(walletReversalReqDto,
				balanceProcessorQueue.getMerchantId());
		return transfer;
	}

	public String commDeductedAmt(String amount, String trType, String gateway) throws JsonProcessingException {
		double amt = Double.parseDouble(amount);
		String adder = "10.00";
		if (amt > 500.00) {
			adder = String.valueOf((amt * 2)/ 100);
		}
		return String.valueOf(Double.parseDouble(adder) + amt);
		// return amount;
		// logger.info(amount + "|" + trType + "|" + gateway);
		// List<CommissionsDeduction> cd = cdr.findByGatewayAndType(gateway,
		// trType.toUpperCase());
		// double amt = Double.parseDouble(amount);
		// String adder = "0.00";
		// logger.info(Utility.convertDTO2JsonString(cd));
		// if (cd.size() > 0) {
		// if (amt < 1001) {
		// adder = searchList(cd, "1000");
		// } else if ((amt > 1000) && (amt < 25000)) {
		// adder = searchList(cd, "25000");
		// } else if (amt > 25001) {
		// adder = searchList(cd, "25001");
		// }
		// }
		// logger.info("ADDER::" + adder);
		// if (adder != null) {
		// return String.valueOf(Double.parseDouble(adder) + amt);
		// } else {
		// logger.info("AMT::" + amt);
		// return String.valueOf(amt);
		// }

	}

	private String searchList(List<CommissionsDeduction> l, String s) {
		for (CommissionsDeduction ls : l) {

			if (ls.getSlab().contains(s)) {
				return ls.getValue();
			}
		}
		return null;
	}

	public boolean txnParam(String val) {
		if (val == null || val.isBlank() || val.isEmpty()) {
			return false;
		}
		return true;
	}

	public Object transactionFilter(TransactionFilterReq dto)
			throws  ValidationExceptions, ParseException {
		String dateTo = dto.getToDate();
		String dateFrom = dto.getFromDate();
		String orderId = dto.getOrderId();
		String bankaccount = dto.getBankaccount();
		String beneficiaryName = dto.getBeneficiaryName();
		String ifsc = dto.getIfsc();
		String merchantId = dto.getMerchantId();
		String status = dto.getStatus();
		String transactionType = dto.getTransactionType();

		if (txnParam(dateFrom) == true && txnParam(dateTo) == true) {
			if (!Validator.isValidateDateFormat(dateTo) || !Validator.isValidateDateFormat(dateFrom)) {
				throw new ValidationExceptions(DATE_FORMAT_VALIDATION,
						FormValidationExceptionEnums.DATE_FORMAT_VALIDATION);
			}

			dateFrom = Utility.convertDatetoMySqlDateFormat(dateFrom);
			dateTo = Utility.convertDatetoMySqlDateFormat(dateTo);
		}

		if (txnParam(status) == true) {
			if (!Validator.containsEnum(TransactionStatus.class, status)) {
				throw new ValidationExceptions(TRANSACTION_STATUS, FormValidationExceptionEnums.INPUT_VALIDATION_ERROR);
			}
		}

		if (txnParam(transactionType) == true) {
			if (!Validator.containsEnum(TransactionType.class, transactionType)) {
				throw new ValidationExceptions(TRANSACTION_TYPE, FormValidationExceptionEnums.INPUT_VALIDATION_ERROR);
			}
		}

		// if(txnParam(bankaccount)==true) {
		// if(!Validator.isValidAccountNumber(bankaccount)) {
		// throw new ValidationExceptions(ACCOUNT_NUMBER_VAIDATION_FAILED,
		// FormValidationExceptionEnums.INPUT_VALIDATION_ERROR);
		// }}
		//
		// if(txnParam(ifsc)==true) {
		// if(!Validator.isValidIfsc(ifsc)) {
		// throw new ValidationExceptions(IFSC_VAIDATION_FAILED,
		// FormValidationExceptionEnums.INPUT_VALIDATION_ERROR);
		// }}

		logger.info("bank name :" + dto.getBeneficiaryName() + "bank account : " + dto.getBankaccount() + " ifsc "
				+ dto.getIfsc());

		List<TransactionDetails> listTransactionDetals = new ArrayList<>();

		if (txnParam(merchantId) == true && txnParam(status) == true && txnParam(transactionType) == true
				&& txnParam(dateTo) == false && txnParam(dateFrom) == false) {
			logger.info("this time ,inside the id and status and type function");
			listTransactionDetals = transactionDetailsRepository.getByStatusAndMerchantAndType(status, transactionType,
					merchantId);
		} else if (txnParam(merchantId) == true && txnParam(status) == true && txnParam(dateTo) == false
				&& txnParam(dateFrom) == false) {
			listTransactionDetals = transactionDetailsRepository.findByTransactionStatusAndMerchantId(status,
					merchantId);
		} else if (txnParam(merchantId) == true && txnParam(transactionType) == true && txnParam(dateTo) == false
				&& txnParam(dateFrom) == false) {
			listTransactionDetals = transactionDetailsRepository
					.getSearchByMerchantIdAndTransactionType(transactionType, merchantId);
		} else if (txnParam(merchantId) == true && txnParam(orderId) == true && txnParam(status) == true
				&& txnParam(transactionType) == true && txnParam(dateTo) == false && txnParam(dateFrom) == false) {
			listTransactionDetals = transactionDetailsRepository
					.getSearchByMerchantIdAndStatusAndTransactionType(status, transactionType, merchantId, orderId);
		} else if (txnParam(merchantId) == true) {
			listTransactionDetals = transactionDetailsRepository.getByMerchantId(merchantId);
		} else if (txnParam(bankaccount) == true && txnParam(beneficiaryName) == true && txnParam(ifsc) == true
				&& txnParam(dateTo) == false && txnParam(dateFrom) == false) {
			listTransactionDetals = transactionDetailsRepository.findByBankaccountAndBeneficiaryNameAndIfsc(bankaccount,
					beneficiaryName, ifsc);
		} else if (txnParam(bankaccount) == true && txnParam(beneficiaryName) == true && txnParam(dateTo) == false
				&& txnParam(dateFrom) == false) {
			listTransactionDetals = transactionDetailsRepository.findByBankaccountAndBeneficiaryName(bankaccount,
					beneficiaryName);
		} else if (txnParam(beneficiaryName) == true && txnParam(ifsc) == true && txnParam(dateTo) == false
				&& txnParam(dateFrom) == false) {
			listTransactionDetals = transactionDetailsRepository.findByBeneficiaryNameAndIfsc(beneficiaryName, ifsc);
		} else if (txnParam(orderId) == true && (orderId.length() > 2)) {
			listTransactionDetals = transactionDetailsRepository.getByOrderId(orderId);
		} else if (txnParam(dateFrom) == true && txnParam(dateTo) == true && txnParam(bankaccount) == true) {
			listTransactionDetals = transactionDetailsRepository.getByDateAndBankAccount(dateFrom, dateTo, bankaccount);
		} else if (txnParam(bankaccount) == true) {
			listTransactionDetals = transactionDetailsRepository.findByBankaccount(bankaccount);
		} else if (txnParam(beneficiaryName) == true) {
			listTransactionDetals = transactionDetailsRepository.findByBeneficiaryName(beneficiaryName);
		} else if (txnParam(ifsc) == true) {
			listTransactionDetals = transactionDetailsRepository.findByIfsc(ifsc);
		} else if (txnParam(dateFrom) == true && txnParam(dateTo) == true && txnParam(status) == true) {
			listTransactionDetals = transactionDetailsRepository.getByDateAndTransactionStatus(dateFrom, dateTo,
					status);
		} else if (txnParam(dateFrom) == true && txnParam(dateTo) == true && txnParam(transactionType) == true) {
			listTransactionDetals = transactionDetailsRepository.getByDateAndTransactionType(dateFrom, dateTo,
					transactionType);
		} else if (txnParam(status) == true) {
			listTransactionDetals = transactionDetailsRepository.findByTransactionStatus(status);
		} else if (txnParam(transactionType) == true) {
			listTransactionDetals = transactionDetailsRepository.getSearchByTransactionType(transactionType);
		} else if (txnParam(dateFrom) == true && txnParam(dateTo) == true) {
			listTransactionDetals = transactionDetailsRepository.getTransactionDateRange(dateFrom, dateTo);
		} else if (txnParam(dateFrom) == true && txnParam(dateTo) == false) {
			dateFrom = Utility.convertDatetoMySqlDateFormat(dateFrom);
			listTransactionDetals = transactionDetailsRepository.getTransactionDateFrom(dateFrom);
		} else if (txnParam(dateTo) == true && txnParam(dateFrom) == false) {
			dateTo = Utility.convertDatetoMySqlDateFormat(dateTo);
			listTransactionDetals = transactionDetailsRepository.getTransactionDateTo(dateTo);
		}

		if (listTransactionDetals.isEmpty()) {
			throw new ValidationExceptions(DATA_NOT_FOUND,
					FormValidationExceptionEnums.REQUIRED_INFORMATION_NOT_FOUND);
		}

		List<TransactionDetailsDto> trdetails = new ArrayList<>();
		for (TransactionDetails tr : listTransactionDetals) {
			TransactionDetailsDto trd = new TransactionDetailsDto();
			trd.setUtrId(tr.getUtrid());
			trd.setAmount(tr.getAmount());
			trd.setBankaccount(tr.getBankaccount());
			trd.setTrDateTime(tr.getCreated().toString());
			trd.setLastUpdate(tr.getUpdated().toString());
			trd.setBeneficiaryName(tr.getBeneficiaryName());
			trd.setIfsc(tr.getIfsc());
			trd.setMerchantId(tr.getMerchantId());
			trd.setMerchantOrderId(tr.getOrderId());
			trd.setPgOrderId(tr.getPaytmTransactionId());
			trd.setPhonenumber(tr.getPhonenumber());
			trd.setPurpose(tr.getPurpose());
			trd.setTransactionMessage(tr.getTransactionMessage());
			trd.setTransactionStatus(tr.getTransactionStatus());
			trd.setTransactionType(tr.getTransactionType());
			trd.setBeneficiaryVPA(tr.getBeneficiaryVPA());
			trd.setPgName(tr.getPgname());
			trd.setReferenceId(tr.getReferenceId());
			trd.setErrorMessage(tr.getErrorMessage());
			trdetails.add(trd);
		}

		SuccessResponseDto sdto = new SuccessResponseDto();
		sdto.getMsg().add("Request Processed Successfully !");
		sdto.setSuccessCode(SuccessCode.API_SUCCESS);
		sdto.getExtraData().put("transactionDetails", trdetails);
		return sdto;
	}

	public Object walletReportFilter(WalletFilterReq dto)
			throws  ValidationExceptions, ParseException {
		String dateFrom = dto.getFromDate();
		String dateTo = dto.getToDate();
		String merchantId = dto.getMerchantId();
		String creditDebit = dto.getCreditDebit();
		String walletId = dto.getWalletId();
		String status = dto.getStatus();
		String transactionType = dto.getTransactionType();
		String transactionId = dto.getTransactionId();

		if (txnParam(dateFrom) == true && txnParam(dateTo) == true) {
			if (!Validator.isValidateDateFormat(dateTo) || !Validator.isValidateDateFormat(dateFrom)) {
				throw new ValidationExceptions(DATE_FORMAT_VALIDATION,
						FormValidationExceptionEnums.DATE_FORMAT_VALIDATION);
			}

			dateFrom = Utility.convertDatetoMySqlDateFormat(dateFrom);
			dateTo = Utility.convertDatetoMySqlDateFormat(dateTo);
		}

		if (txnParam(status) == true) {
			if (!Validator.containsEnum(WalletStatus.class, status)) {
				throw new ValidationExceptions(WALLET_STATUS, FormValidationExceptionEnums.INPUT_VALIDATION_ERROR);
			}
		}

		if (txnParam(transactionType) == true) {
			if (!Validator.containsEnum(WalletType.class, transactionType)) {
				throw new ValidationExceptions(WALLET_TYPE, FormValidationExceptionEnums.INPUT_VALIDATION_ERROR);
			}
		}

		List<WalletTransactions> list = new ArrayList<WalletTransactions>();

		if (txnParam(merchantId) == true && txnParam(status) == true && txnParam(transactionType) == true
				&& txnParam(dateFrom) == false && txnParam(dateTo) == false) {
			list = walletTransactionsRepo.getByStatusAndTypeAndId(status, transactionType, merchantId);
		} else if (txnParam(merchantId) == true && txnParam(status) == true && txnParam(dateFrom) == false
				&& txnParam(dateTo) == false) {
			list = walletTransactionsRepo.findByTransactionStatusAndMerchantId(status, merchantId);
		} else if (txnParam(merchantId) == true && txnParam(transactionType) == true && txnParam(dateFrom) == false
				&& txnParam(dateTo) == false) {
			list = walletTransactionsRepo.getSearchByMerchantIdAndTransactionType(transactionType, merchantId);
		} else if (txnParam(merchantId) == true && txnParam(creditDebit) == true && txnParam(dateFrom) == true
				&& txnParam(dateTo) == true) {
			list = walletTransactionsRepo.getByDateAndCreditDebitAndMerchantId(dateFrom, dateTo, creditDebit,
					merchantId);
		} else if (txnParam(creditDebit) == true && txnParam(dateFrom) == true && txnParam(dateTo) == true) {
			list = walletTransactionsRepo.getByDateAndCreditDebit(dateFrom, dateTo, creditDebit);
		} else if (txnParam(merchantId) == true && txnParam(creditDebit) == true && txnParam(dateFrom) == false
				&& txnParam(dateTo) == false) {
			list = walletTransactionsRepo.getByCreditDebitAndMerchantId(creditDebit, merchantId);
		} else if (txnParam(merchantId) == true) {
			list = walletTransactionsRepo.getByMerchantId(merchantId);
		} else if (txnParam(transactionId) == true) {
			list = walletTransactionsRepo.findByTransactionId(transactionId);
		} else if (txnParam(walletId) == true) {
			list = walletTransactionsRepo.getByWalletid(walletId);
		} else if (txnParam(creditDebit) == true) {
			list = walletTransactionsRepo.getByCreditDebit(creditDebit);
		} else if (txnParam(dateFrom) == true && txnParam(dateTo) == true && txnParam(status) == true) {
			list = walletTransactionsRepo.getByDateAndTransactionStatus(dateFrom, dateTo, status);
		} else if (txnParam(dateFrom) == true && txnParam(dateTo) == true && txnParam(transactionType) == true) {
			list = walletTransactionsRepo.getByDateAndTransactionType(dateFrom, dateTo, transactionType);
		} else if (txnParam(status) == true) {
			list = walletTransactionsRepo.findByTransactionStatus(status);
		} else if (txnParam(transactionType) == true) {
			list = walletTransactionsRepo.getSearchByTransactionType(transactionType);
		} else if (txnParam(dateFrom) == true && txnParam(dateTo) == true) {
			list = walletTransactionsRepo.getTransactionDateRange(dateFrom, dateTo);
		} else if (txnParam(dateFrom) == true && txnParam(dateTo) == false) {
			dateFrom = Utility.convertDatetoMySqlDateFormat(dateFrom);
			list = walletTransactionsRepo.getByCreatedfrom(dateFrom);
		} else if (txnParam(dateTo) == true && txnParam(dateFrom) == false) {
			dateTo = Utility.convertDatetoMySqlDateFormat(dateTo);
			list = walletTransactionsRepo.getTransactionDateTo(dateTo);
		}

		if (list.isEmpty()) {
			throw new ValidationExceptions(DATA_NOT_FOUND,
					FormValidationExceptionEnums.REQUIRED_INFORMATION_NOT_FOUND);
		}

		SuccessResponseDto sdto = new SuccessResponseDto();
		sdto.getMsg().add("Request Processed Successfully !");
		sdto.setSuccessCode(SuccessCode.API_SUCCESS);
		sdto.getExtraData().put("transactionDetails", list);
		return sdto;

	}

	public Object AllWalletReport() {
		SuccessResponseDto sdto = new SuccessResponseDto();
		sdto.getMsg().add("Request Processed Successfully !");
		sdto.setSuccessCode(SuccessCode.API_SUCCESS);
		sdto.getExtraData().put("transactionDetails", walletTransactionsRepo.findAll());
		return sdto;
	}

	public Object AllTransactionReport() {
		SuccessResponseDto sdto = new SuccessResponseDto();
		sdto.getMsg().add("Request Processed Successfully !");
		sdto.setSuccessCode(SuccessCode.API_SUCCESS);
		sdto.getExtraData().put("transactionDetails", transactionDetailsRepository.findAll());
		return sdto;
	}

	@Async
    public void accountTransferUsingBulkFile(List<AccountTransferMerReq> dto, String merchantid) {
		dto.forEach(o->{
			try {
				accountTransfer(o,merchantid);
			} catch (JsonProcessingException | ValidationExceptions | ParseException e) {
				e.printStackTrace();
			}
		});
    }

	@Async
	public void accountTransferUPIUsingBulkFile(List<AccountTransferUPIMerReq> dto, String merchantid) {
		dto.forEach(o->{
			try {
				accountTransferUPI(o,merchantid);
			} catch (JsonProcessingException | ParseException | ValidationExceptions e) {
				e.printStackTrace();
			}
		});
	}


}
