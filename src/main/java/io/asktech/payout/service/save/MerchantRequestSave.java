package io.asktech.payout.service.save;

import java.text.ParseException;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.asktech.payout.dto.merchant.AccountTransferMerReq;
import io.asktech.payout.dto.merchant.AccountTransferUPIMerReq;
import io.asktech.payout.dto.merchant.BankAccountVerifyRequest;
import io.asktech.payout.dto.merchant.WalletTransferMerReq;
import io.asktech.payout.dto.reqres.WalletTransferRes;
import io.asktech.payout.enums.FormValidationExceptionEnums;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.modal.merchant.AccountTransactionReq;
import io.asktech.payout.modal.merchant.AccountTransactionUPIReq;
import io.asktech.payout.modal.merchant.BankAccountVerifyReqDetails;
import io.asktech.payout.modal.merchant.BankAccountVerifyResDetails;
import io.asktech.payout.modal.merchant.TransactionDetails;
import io.asktech.payout.modal.merchant.WalletTransactionReq;
import io.asktech.payout.modal.merchant.WalletTransactionRes;
import io.asktech.payout.paytm.requests.dto.PaytmAccountVerifyResponse;
import io.asktech.payout.repository.merchant.AccountTransactionReqRepo;
import io.asktech.payout.repository.merchant.AccountTransactionUPIReqRepo;
import io.asktech.payout.repository.merchant.BankAccountVerifyReqDetailsRepo;
import io.asktech.payout.repository.merchant.BankAccountVerifyResDetailsRepo;
import io.asktech.payout.repository.merchant.TransactionDetailsRepo;
import io.asktech.payout.repository.merchant.WalletTransactionReqRepo;
import io.asktech.payout.repository.merchant.WalletTransactionResRepo;
import io.asktech.payout.utils.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Component
public class MerchantRequestSave {
	@Autowired
	WalletTransactionReqRepo walletTransactionReqRepo;
	@Autowired
	AccountTransactionReqRepo accountTransactionReqRepo;
	@Autowired
	WalletTransactionResRepo walletTransactionResRepo;
	@Autowired
	BankAccountVerifyReqDetailsRepo bankAccountVerifyReqDetailsRepo;
	@Autowired
	BankAccountVerifyResDetailsRepo bankAccountVerifyResDetailsRepo;
	@Autowired
	AccountTransactionUPIReqRepo accountTransactionUPIReqRepo;
	static Logger logger = LoggerFactory.getLogger(MerchantRequestSave.class);
	public boolean MerchantWalletTransferRequest(WalletTransferMerReq dto, String merchantId)
			throws ValidationExceptions {
		WalletTransactionReq walletTransactionReq = new WalletTransactionReq();
		walletTransactionReq.setOrderId(dto.getOrderid());
		walletTransactionReq.setAmount(dto.getAmount());
		walletTransactionReq.setPhonenumber(dto.getPhonenumber());
		walletTransactionReq.setMerchantId(merchantId);
		walletTransactionReq.setInternalOrderId(dto.getInternalOrderId());
		try {
			walletTransactionReqRepo.save(walletTransactionReq);
		} catch (Exception e) {
			throw new ValidationExceptions("SQL Exception::"+e.getMessage(), FormValidationExceptionEnums.SQLERROR_EXCEPTION);
		}
		return true;

	}

	public boolean MerchantWalletTransferResponse(WalletTransferRes dto, String orderId, String merchantId,
			String txType, String internalOrderId) throws ValidationExceptions, JsonProcessingException {
		logger.info("WalletTransferRes--" + Utility.convertDTO2JsonString(dto));
		WalletTransactionRes walletTransactionRes = new WalletTransactionRes();
		walletTransactionRes.setOrderId(orderId);
		walletTransactionRes.setMerchantId(merchantId);
		walletTransactionRes.setStatus(dto.getStatus());
		walletTransactionRes.setStatusCode(dto.getStatusCode());
		walletTransactionRes.setStatusMessage(dto.getStatusMessage());
		walletTransactionRes.setTxType(txType);
		walletTransactionRes.setInternalOrderId(internalOrderId);

		try {
			walletTransactionResRepo.save(walletTransactionRes);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidationExceptions("SQL Exception::"+e.getMessage(), FormValidationExceptionEnums.SQLERROR_EXCEPTION);

		}
		return true;

	}

	public boolean MerchantAccountTransferRequest(AccountTransferUPIMerReq dto, String merchantId)
			throws ValidationExceptions {
		AccountTransactionUPIReq accountTransactionReq = new AccountTransactionUPIReq();
		accountTransactionReq.setOrderId(dto.getOrderid());
		accountTransactionReq.setInternalOrderId(dto.getInternalOrderid());
		accountTransactionReq.setAmount(dto.getAmount());
		accountTransactionReq.setPhonenumber(dto.getPhonenumber());
		accountTransactionReq.setMerchantId(merchantId);
		accountTransactionReq.setPurpose(dto.getPurpose());
		accountTransactionReq.setRequestType(dto.getRequestType());
		accountTransactionReq.setBeneficiaryVPA(dto.getBeneficiaryVPA());
		accountTransactionReq.setBeneficiaryName(dto.getBeneficiaryName());
		accountTransactionReq.setRequestStatus(dto.getReqStatus());
		accountTransactionReq.setPgName(dto.getPgName());
		accountTransactionReq.setRequestType(dto.getRequestType());
		accountTransactionReq.setThreadFlag(Utility.randomInteger());
		try {
			accountTransactionUPIReqRepo.save(accountTransactionReq);
		} catch (Exception e) {			
			logger.error("SQL Exception"+e.getMessage());
			e.printStackTrace();
			throw new ValidationExceptions("SQL Exception::"+e.getMessage(), FormValidationExceptionEnums.SQLERROR_EXCEPTION);

		}
		return true;

	}

	public boolean MerchantAccountTransferRequest(AccountTransferMerReq dto, String merchantId)
			throws ValidationExceptions {
		AccountTransactionReq accountTransactionReq = new AccountTransactionReq();
		accountTransactionReq.setOrderId(dto.getOrderid());
		accountTransactionReq.setInternalOrderId(dto.getInternalOrderid());
		accountTransactionReq.setAmount(dto.getAmount());
		accountTransactionReq.setPhonenumber(dto.getPhonenumber());
		accountTransactionReq.setMerchantId(merchantId);
		accountTransactionReq.setBankaccount(dto.getBankaccount());
		accountTransactionReq.setIfsc(dto.getIfsc());
		accountTransactionReq.setPurpose(dto.getPurpose());
		accountTransactionReq.setBeneficiaryName(dto.getBeneficiaryName());
		accountTransactionReq.setRequestType(dto.getRequestType());
		accountTransactionReq.setPgName(dto.getPgName());
		accountTransactionReq.setRequestStatus(dto.getReqStatus());
		accountTransactionReq.setThreadFlag(Utility.randomInteger());
		try {
			accountTransactionReqRepo.save(accountTransactionReq);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidationExceptions("SQL Exception::"+e.getMessage(), FormValidationExceptionEnums.SQLERROR_EXCEPTION);

		}
		return true;

	}

	@Autowired
	TransactionDetailsRepo transactionDetailsRepo;

	public boolean MerchantTransactionDetails(WalletTransferMerReq dto, WalletTransferRes wallettransferRes,
			String merchantId) throws ValidationExceptions {
		TransactionDetails transactionDetails = null;
		transactionDetails = transactionDetailsRepo.findByInternalOrderId(dto.getInternalOrderId());
		if (transactionDetails == null) {
			transactionDetails = new TransactionDetails();
		}
		transactionDetails.setOrderId(dto.getOrderid());
		transactionDetails.setInternalOrderId(dto.getInternalOrderId());
		transactionDetails.setAmount(dto.getAmount());
		transactionDetails.setPhonenumber(dto.getPhonenumber());
		transactionDetails.setMerchantId(merchantId);
		transactionDetails.setTransactionType("WALLET");
		transactionDetails.setTransactionStatus(wallettransferRes.getStatus());
		transactionDetails.setPgname(dto.getPgName());
		transactionDetails.setPgId(dto.getPgId());
		if ((wallettransferRes.getReferenceId() == null)
				|| (wallettransferRes.getReferenceId().trim().equalsIgnoreCase(""))) {
			transactionDetails.setPaytmTransactionId(dto.getInternalOrderId());
		} else {
			transactionDetails.setPaytmTransactionId(wallettransferRes.getReferenceId());
		}
		transactionDetails.setTransactionMessage(wallettransferRes.getStatusMessage());
		if (wallettransferRes.getUtr() != null) {
			transactionDetails.setUtrid(wallettransferRes.getUtr());
		}
		try {
			transactionDetailsRepo.save(transactionDetails);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidationExceptions("SQL Exception::"+e.getMessage(), FormValidationExceptionEnums.SQLERROR_EXCEPTION);

		}
		return true;

	}

	public boolean MerchantTransactionDetails(AccountTransferMerReq dto, WalletTransferRes wallettransferRes,
			String merchantId) throws ValidationExceptions {
		
		TransactionDetails transactionDetails = null;
		transactionDetails = transactionDetailsRepo.findByInternalOrderId(dto.getInternalOrderid());
		if (transactionDetails == null) {
			transactionDetails = new TransactionDetails();
		}
		transactionDetails.setOrderId(dto.getOrderid());
		transactionDetails.setInternalOrderId(dto.getInternalOrderid());
		transactionDetails.setAmount(dto.getAmount());
		transactionDetails.setPhonenumber(dto.getPhonenumber());
		transactionDetails.setMerchantId(merchantId);
		transactionDetails.setBankaccount(dto.getBankaccount());
		transactionDetails.setIfsc(dto.getIfsc());
		transactionDetails.setTransactionType(dto.getRequestType());
		transactionDetails.setTransactionStatus(wallettransferRes.getStatus());
		transactionDetails.setPaytmTransactionId(dto.getInternalOrderid());
		transactionDetails.setTransactionMessage(wallettransferRes.getStatusMessage());
		transactionDetails.setBeneficiaryName(dto.getBeneficiaryName());
		transactionDetails.setPurpose(dto.getPurpose());
		transactionDetails.setPgname(dto.getPgName());
		transactionDetails.setPgId(dto.getPgid());
		if(wallettransferRes.getErrorMsg() != null){
			transactionDetails.setErrorMessage(wallettransferRes.getErrorMsg());
		}
		if ((wallettransferRes.getReferenceId() == null)) {
			transactionDetails.setPaytmTransactionId(dto.getInternalOrderid());
		} else {
			transactionDetails.setPaytmTransactionId(wallettransferRes.getReferenceId());
			transactionDetails.setReferenceId(wallettransferRes.getReferenceId());
		}
		if (wallettransferRes.getUtr() != null) {
			transactionDetails.setUtrid(wallettransferRes.getUtr());
		}
		try {
			transactionDetailsRepo.save(transactionDetails);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("SQL Error::"+e.getMessage());
			throw new ValidationExceptions("SQL Exception::"+e.getMessage(), FormValidationExceptionEnums.SQLERROR_EXCEPTION);

		}
		return true;

	}

	public TransactionDetails MerchantTransactionDetails(AccountTransferMerReq dto, String merchantId)
			throws ValidationExceptions {
		TransactionDetails transactionDetails = null;
		transactionDetails = transactionDetailsRepo.findByInternalOrderId(dto.getInternalOrderid());
		if (transactionDetails == null) {
			transactionDetails = new TransactionDetails();
		}
			transactionDetails.setOrderId(dto.getOrderid());
			transactionDetails.setInternalOrderId(dto.getInternalOrderid());
			transactionDetails.setAmount(dto.getAmount());
			transactionDetails.setPhonenumber(dto.getPhonenumber());
			transactionDetails.setMerchantId(merchantId);
			transactionDetails.setBankaccount(dto.getBankaccount());
			transactionDetails.setIfsc(dto.getIfsc());
			transactionDetails.setTransactionType(dto.getRequestType());
			transactionDetails.setTransactionStatus(dto.getReqStatus());
			transactionDetails.setPaytmTransactionId(dto.getInternalOrderid());
			transactionDetails.setTransactionMessage(dto.getTrRemark());
			transactionDetails.setBeneficiaryName(dto.getBeneficiaryName());
			transactionDetails.setPurpose(dto.getPurpose());
			transactionDetails.setPgname(dto.getPgName());
			transactionDetails.setPgId(dto.getPgid());
			
		try {
			return transactionDetailsRepo.save(transactionDetails);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("SQL Error::"+e.getMessage());
			throw new ValidationExceptions("SQL Exception::"+e.getMessage(), FormValidationExceptionEnums.SQLERROR_EXCEPTION);

		}
	}

	public boolean merchantTransactionDetailsUPI(AccountTransferUPIMerReq dto, WalletTransferRes wallettransferRes,
			String merchantId) throws ValidationExceptions {
		TransactionDetails transactionDetails = null;
		transactionDetails = transactionDetailsRepo.findByInternalOrderId(dto.getInternalOrderid());
		if (transactionDetails == null) {
			transactionDetails = new TransactionDetails();
		}
		transactionDetails.setOrderId(dto.getOrderid());
		transactionDetails.setInternalOrderId(dto.getInternalOrderid());
		transactionDetails.setAmount(dto.getAmount());
		transactionDetails.setPhonenumber(dto.getPhonenumber());
		transactionDetails.setMerchantId(merchantId);
		transactionDetails.setBeneficiaryVPA(dto.getBeneficiaryVPA());
		transactionDetails.setTransactionType(dto.getRequestType());
		transactionDetails.setTransactionStatus(wallettransferRes.getStatus());
		transactionDetails.setPaytmTransactionId(dto.getInternalOrderid());
		transactionDetails.setTransactionMessage(wallettransferRes.getStatusMessage());
		transactionDetails.setPurpose(dto.getPurpose());
		transactionDetails.setPgname(dto.getPgName());
		transactionDetails.setPgId(dto.getPgId());
		if(wallettransferRes.getErrorMsg() != null){
			transactionDetails.setErrorMessage(wallettransferRes.getErrorMsg());
		}
		if ((wallettransferRes.getReferenceId() == null)) {
			transactionDetails.setPaytmTransactionId(dto.getInternalOrderid());
		} else {
			transactionDetails.setPaytmTransactionId(wallettransferRes.getReferenceId());
			transactionDetails.setReferenceId(wallettransferRes.getReferenceId());
		}
		if (wallettransferRes.getUtr() != null) {
			transactionDetails.setUtrid(wallettransferRes.getUtr());
		}
		try {
			transactionDetailsRepo.save(transactionDetails);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("SQL Error::"+e.getMessage());
			throw new ValidationExceptions("SQL Exception::"+e.getMessage(), FormValidationExceptionEnums.SQLERROR_EXCEPTION);

		}
		return true;

	}
	
	public TransactionDetails merchantTransactionDetailsUPI(AccountTransferUPIMerReq dto,
			String merchantId) throws ValidationExceptions {
		TransactionDetails transactionDetails = null;
		transactionDetails = transactionDetailsRepo.findByInternalOrderId(dto.getInternalOrderid());
		if (transactionDetails == null) {
			transactionDetails = new TransactionDetails();
		}
		transactionDetails.setOrderId(dto.getOrderid());
		transactionDetails.setInternalOrderId(dto.getInternalOrderid());
		transactionDetails.setAmount(dto.getAmount());
		transactionDetails.setPhonenumber(dto.getPhonenumber());
		transactionDetails.setMerchantId(merchantId);
		transactionDetails.setBeneficiaryVPA(dto.getBeneficiaryVPA());
		transactionDetails.setBeneficiaryName(dto.getBeneficiaryName());
		transactionDetails.setTransactionType(dto.getRequestType());
		transactionDetails.setTransactionStatus(dto.getReqStatus());
		transactionDetails.setPaytmTransactionId(dto.getInternalOrderid());
		transactionDetails.setTransactionMessage(dto.getTrRemark());
		transactionDetails.setPurpose(dto.getPurpose());
		transactionDetails.setPgname(dto.getPgName());
		transactionDetails.setPgId(dto.getPgId());
		try {
			return transactionDetailsRepo.save(transactionDetails);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("SQL Error::"+e.getMessage());
			throw new ValidationExceptions("SQL Exception::"+e.getMessage(), FormValidationExceptionEnums.SQLERROR_EXCEPTION);

		}
		

	}

	public boolean bankAccountVerifyReqDetails(BankAccountVerifyRequest bankAccountVerifyRequest)
			throws ParseException, ValidationExceptions {

		BankAccountVerifyReqDetails bankAccountVerifyReqDetails = new BankAccountVerifyReqDetails();
		bankAccountVerifyReqDetails.setBankIFSCCode(bankAccountVerifyRequest.getBeneficiaryIFSCCode());
		bankAccountVerifyReqDetails.setBeneficiaryBankAccount(bankAccountVerifyRequest.getBeneficiaryAccountNo());
		bankAccountVerifyReqDetails.setInternalOrderId(bankAccountVerifyRequest.getInternalOrderId());
		bankAccountVerifyReqDetails.setMerchantId(bankAccountVerifyRequest.getMerchantId());
		bankAccountVerifyReqDetails.setOrderId(bankAccountVerifyRequest.getOrderId());
		bankAccountVerifyReqDetails.setBeneficiaryVPA(bankAccountVerifyRequest.getBeneficiaryVPA());
		try {
			bankAccountVerifyReqDetailsRepo.save(bankAccountVerifyReqDetails);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("SQL Error::"+e.getMessage());
			throw new ValidationExceptions("SQL Exception::"+e.getMessage(), FormValidationExceptionEnums.SQLERROR_EXCEPTION);

		}

		return true;
	}

	public boolean bankAccountVerifyResDetails(BankAccountVerifyRequest bankAccountVerifyRequest,
			PaytmAccountVerifyResponse paytmAccountVerifyResponse) throws ParseException, ValidationExceptions {

		BankAccountVerifyResDetails bankAccountVerifyResDetails = new BankAccountVerifyResDetails();
		bankAccountVerifyResDetails.setInternalOrderId(String.valueOf(Utility.getEpochTIme()));
		bankAccountVerifyResDetails.setMerchantId(bankAccountVerifyRequest.getMerchantId());
		bankAccountVerifyResDetails.setOrderId(bankAccountVerifyRequest.getOrderId());
		bankAccountVerifyResDetails.setStatus(paytmAccountVerifyResponse.getStatus());
		bankAccountVerifyResDetails.setStatusCode(paytmAccountVerifyResponse.getStatusCode());
		bankAccountVerifyResDetails.setStatusMessage(paytmAccountVerifyResponse.getStatusMessage());

		try {
			bankAccountVerifyResDetailsRepo.save(bankAccountVerifyResDetails);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("SQL Error::"+e.getMessage());
			throw new ValidationExceptions("SQL Exception::"+e.getMessage(), FormValidationExceptionEnums.SQLERROR_EXCEPTION);

		}
		return true;
	}

	public boolean PayTmResponse() {
		return true;
	}

}
