package io.asktech.payout.service;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.asktech.payout.constants.PaytmWalletConstants;
import io.asktech.payout.dto.merchant.AccountTransferMerReq;
import io.asktech.payout.dto.merchant.AccountTransferUPIMerReq;
import io.asktech.payout.dto.reqres.WalletTransferRes;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.modal.merchant.AccountTransactionReq;
import io.asktech.payout.modal.merchant.AccountTransactionUPIReq;
import io.asktech.payout.modal.merchant.PgDetails;
import io.asktech.payout.modal.merchant.TransactionDetails;
import io.asktech.payout.repository.merchant.AccountTransactionReqRepo;
import io.asktech.payout.repository.merchant.AccountTransactionUPIReqRepo;
import io.asktech.payout.repository.merchant.TransactionDetailsRepo;
import io.asktech.payout.schedular.PayoutPaymentAPI;
import io.asktech.payout.service.save.MerchantRequestSave;
import io.asktech.payout.utils.Utility;

@Component
public class WalletTransaferAsync {
	static Logger logger = LoggerFactory.getLogger(PayoutPaymentAPI.class);
	@Autowired
	PayoutWalletAdminService payoutWalletAdminService;
	@Autowired
	MerchantRequestSave merchantRequestSave;
	@Autowired
	PayoutMerchant payoutMerchant;
	@Autowired
	AccountTransactionReqRepo accountTransactionReqRepo;
	@Autowired
	AccountTransactionUPIReqRepo accountTransactionUPIReqRepo;
	@Autowired
	NotiFyURLService2Merchant notiFyURLService2Merchant;
	@Autowired
	PayoutUtils payoutUtils;

	@Autowired
	TransactionDetailsRepo transactionDetailsRepo;

	@Async
	public void accountTransfer(AccountTransferMerReq dto, String merchantId, AccountTransactionReq ar)
			throws ValidationExceptions, JsonProcessingException {
		logger.info("Account Transfer Balance Deduction success");
		WalletTransferRes wallettransferRes = new WalletTransferRes();
		String status = PaytmWalletConstants.PENDING;
		String message = "Unable to Process";
		String error = "";
		PgDetails gatewayDetails = payoutUtils.getMerchantPgDetails(merchantId, dto.getRequestType());
		if (gatewayDetails == null) {
			status = PaytmWalletConstants.FAILURE;
			message = "Pg mapping not found";
		} else {
			dto.setPgid(gatewayDetails.getPgId());
			try {
				wallettransferRes = payoutWalletAdminService.getAccountTransfer(dto, merchantId, gatewayDetails);
				logger.info("Before check the transaction status :: "
						+ Utility.convertDTO2JsonString(wallettransferRes));
				merchantRequestSave.MerchantTransactionDetails(dto, wallettransferRes, merchantId);

				status = wallettransferRes.getStatus();
				message = wallettransferRes.getStatusMessage();
			} catch (Exception e) {
				status = PaytmWalletConstants.PENDING;
				message = "EX:Transaction Initiated";
				error = ExceptionUtils.getStackTrace(e);
				logger.error(error);
			}

			merchantRequestSave.MerchantWalletTransferResponse(wallettransferRes, dto.getOrderid(), merchantId,
					dto.getRequestType(), dto.getInternalOrderid());
		}
		if (status.equalsIgnoreCase(PaytmWalletConstants.FAILURE)) {
			// payoutMerchant.balanceRefund(dto, merchantId);
			payoutMerchant.populateBalanceProcessRefund(dto, merchantId);
		}
		ar.setRequestStatus(status);
		ar.setTrRemarks(message + "|" + error);
		accountTransactionReqRepo.save(ar);
		dto.setReqStatus(status);
		dto.setTrRemark(message);

		TransactionDetails transactionDetails = merchantRequestSave.MerchantTransactionDetails(dto, merchantId);
		logger.info("transactionDetails save::" + Utility.convertDTO2JsonString(transactionDetails));
		if (transactionDetails.getCallBackURL() != null) {
			transactionDetails.setCallBackFlag("TRUE");
			transactionDetailsRepo.save(transactionDetails);
			logger.info("CALLBACK SENDER ACCTRX::" + transactionDetails.getCallBackURL() + "|"
					+ Utility.convertDTO2JsonString(transactionDetails));
			notiFyURLService2Merchant.sendNotifyDetails2Merchant(transactionDetails);
		}
	}

	@Async
	public void upiTransfer(AccountTransferUPIMerReq dto, String merchantId, AccountTransactionUPIReq ar)
			throws ValidationExceptions, JsonProcessingException {
		WalletTransferRes wallettransferRes = new WalletTransferRes();

		String status = PaytmWalletConstants.FAILURE;
		String message = "Unable to Process";
		String error = "";
		PgDetails gatewayDetails = payoutUtils.getMerchantPgDetails(merchantId, dto.getRequestType().toUpperCase());
		if (gatewayDetails == null) {
			status = PaytmWalletConstants.FAILURE;
			message = "Pg mapping not found";
		} else {
			dto.setPgId(gatewayDetails.getPgId());
			try {
				wallettransferRes = payoutWalletAdminService.getAccountTransferUPI(dto, merchantId, gatewayDetails);
				logger.info("Before check the transaction status :: "
						+ Utility.convertDTO2JsonString(wallettransferRes));
				if (!wallettransferRes.getStatus().equalsIgnoreCase("ERROR")) {
					logger.info("MerchantAccountTransferRequest Failed");
					merchantRequestSave.merchantTransactionDetailsUPI(dto, wallettransferRes, merchantId);
				}
				status = wallettransferRes.getStatus();
				message = wallettransferRes.getStatusMessage();
			} catch (Exception e) {
				status = PaytmWalletConstants.PENDING;
				message = e.getMessage();
				logger.error(message);
				error = ExceptionUtils.getStackTrace(e);
			}
		}
		merchantRequestSave.MerchantWalletTransferResponse(wallettransferRes, dto.getOrderid(), merchantId,
				dto.getRequestType(), dto.getInternalOrderid());
		if (status.equalsIgnoreCase(PaytmWalletConstants.FAILURE)) {
			// payoutMerchant.balanceRefund(dto, merchantId);
			payoutMerchant.populateBalanceProcessRefund(dto, merchantId);
		}
		ar.setRequestStatus(status);
		ar.setTrRemarks(message + "|" + error);
		accountTransactionUPIReqRepo.save(ar);
		dto.setReqStatus(status);
		dto.setTrRemark(message);
		TransactionDetails transactionDetails = merchantRequestSave.merchantTransactionDetailsUPI(dto, merchantId);
		if (transactionDetails.getCallBackURL() != null) {
			logger.info("CALLBACK SENDER UPI::" + transactionDetails.getCallBackURL() + "|"
					+ Utility.convertDTO2JsonString(transactionDetails));
			notiFyURLService2Merchant.sendNotifyDetails2Merchant(transactionDetails);
		}
	}

	public void sendFlaggedNotification() {
		List<TransactionDetails> transactionDetails = transactionDetailsRepo.getByCallBackStatus();
		for (TransactionDetails transactionDetail : transactionDetails) {
			transactionDetail.setCallBackFlag("FALSE");
			transactionDetailsRepo.save(transactionDetail);
			if (transactionDetail.getCallBackURL() != null) {
				if (transactionDetail.getCallBackURL().length() > 5) {
					try {
						logger.info("CALLBACK SENDER ACCTRX::" + transactionDetail.getCallBackURL() + "|"
								+ Utility.convertDTO2JsonString(transactionDetail));
						notiFyURLService2Merchant.sendNotifyDetails2Merchant(transactionDetail);
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}

	}
}
