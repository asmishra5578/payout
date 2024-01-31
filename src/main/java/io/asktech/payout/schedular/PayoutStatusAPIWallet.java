package io.asktech.payout.schedular;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.asktech.payout.dto.merchant.TransactionResponseMerRes;
import io.asktech.payout.dto.reqres.TransferStatusReq;
import io.asktech.payout.enums.ReconStatus;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.modal.merchant.TransactionDetails;
import io.asktech.payout.repository.merchant.TransactionDetailsRepo;
import io.asktech.payout.service.PayoutWalletAdminService;
import io.asktech.payout.service.WalletTransaferAsync;
import io.asktech.payout.service.paytm.requests.PaytmWalletRequests;
import io.asktech.payout.utils.Utility;

@Service
public class PayoutStatusAPIWallet {

	static Logger logger = LoggerFactory.getLogger(PayoutStatusAPIWallet.class);

	@Autowired
	TransactionDetailsRepo transactionDetailsRepo;
	@Autowired
	PaytmWalletRequests paytmWalletRequests;
	@Autowired
	PayoutWalletAdminService payoutWalletAdminService;
	@Autowired
	WalletTransaferAsync walletTransaferAsync;

	public void updateWalletTransactionStatus() throws JsonProcessingException, ValidationExceptions {

		List<TransactionDetails> listTransactionDetails5Mins = transactionDetailsRepo.getAllRecordsForPayout5Minutes();
		populatetransactionStatus(listTransactionDetails5Mins, ReconStatus.R0.toString());

		List<TransactionDetails> listTransactionDetails15Mins = transactionDetailsRepo
				.getAllRecordsForPayout15Minutes();
		populatetransactionStatus(listTransactionDetails15Mins, ReconStatus.R1.toString());

		List<TransactionDetails> listTransactionDetails1Hours = transactionDetailsRepo.getAllRecordsForPayout1Hours();
		populatetransactionStatus(listTransactionDetails1Hours, ReconStatus.R2.toString());

		List<TransactionDetails> listTransactionDetails3Hours = transactionDetailsRepo.getAllRecordsForPayout3Hours();
		populatetransactionStatus(listTransactionDetails3Hours, ReconStatus.R3.toString());

		List<TransactionDetails> listTransactionDetails12Hours = transactionDetailsRepo.getAllRecordsForPayout12Hours();
		populatetransactionStatus(listTransactionDetails12Hours, ReconStatus.R4.toString());

		List<TransactionDetails> listTransactionDetails24Hours = transactionDetailsRepo.getAllRecordsForPayout24Hours();
		populatetransactionStatus(listTransactionDetails24Hours, ReconStatus.R5.toString());

		List<TransactionDetails> listTransactionDetails48Hours = transactionDetailsRepo.getAllRecordsForPayout48Hours();
		populatetransactionStatus(listTransactionDetails48Hours, ReconStatus.R6.toString());
	}

	public void updateWalletTransactionStatusOneTime() throws JsonProcessingException, ValidationExceptions {
		List<TransactionDetails> listTransactionDetails1Time = transactionDetailsRepo.getAllRecordsForPayoutOneTime();
		populatetransactionStatus(listTransactionDetails1Time, ReconStatus.R.toString());
	}

	public void updateWalletTransactionStatusHourly() throws JsonProcessingException, ValidationExceptions {
		logger.info("Get Hourly Transactions");
		List<TransactionDetails> listTransactionDetailsHourly = transactionDetailsRepo.getAllRecordsForPayoutHourly();
		//logger.debug(Utility.convertDTO2JsonString(listTransactionDetailsHourly));
		logger.info("\n"+listTransactionDetailsHourly.size()+"\n");
		populatetransactionStatus(listTransactionDetailsHourly, ReconStatus.RH.toString());
	}


	public void updateWalletTransactionStatusPendingUtrHourly() throws JsonProcessingException, ValidationExceptions {
		logger.info("Get Hourly Transactions");
		List<TransactionDetails> listTransactionDetailsHourly = transactionDetailsRepo.getAllRecordsPendingUTRPayoutHourly();
		//logger.debug(Utility.convertDTO2JsonString(listTransactionDetailsHourly));
		logger.info("\n"+listTransactionDetailsHourly.size()+"\n");
		populatetransactionStatus(listTransactionDetailsHourly, ReconStatus.RH.toString());
	}

	public void populatetransactionStatus(List<TransactionDetails> listTransactionDetails, String reconStatus)
			throws JsonProcessingException, ValidationExceptions {
		for (TransactionDetails transactionDetails : listTransactionDetails) {
			
			TransactionResponseMerRes transactionResponseMerRes = getWalletStatusApiUpdate(transactionDetails);
			transactionDetails.setReconStatus(reconStatus);
			if (transactionResponseMerRes.getStatus() != null) {
				transactionDetails.setTransactionStatus(transactionResponseMerRes.getStatus());
				if (transactionResponseMerRes.getStatusMessage() != null) {
					transactionDetails.setTransactionMessage(transactionResponseMerRes.getStatusMessage());
				}
				if (transactionResponseMerRes.getUtrId() != null) {
					transactionDetails.setUtrid(transactionResponseMerRes.getUtrId());
			}
				if (!transactionResponseMerRes.getStatus().equalsIgnoreCase("ERROR")) {
					transactionDetailsRepo.save(transactionDetails);
				}
			}

			logger.info(
					"Response from Status API Check :: " + Utility.convertDTO2JsonString(transactionResponseMerRes));
		}
	}

	public TransactionResponseMerRes getWalletStatusApiUpdate(TransactionDetails transactionDetails)
			throws JsonProcessingException, ValidationExceptions {

		TransferStatusReq transferStatusReq = new TransferStatusReq();
		transferStatusReq.setOrderId(transactionDetails.getOrderId());
		return payoutWalletAdminService.getWalletTransferStatus(transferStatusReq, transactionDetails.getMerchantId());
		// return paytmWalletRequests.doWalletTransferStatus(transferStatusReq,
		// transactionDetails.getMerchantId());

		// return paytmWalletRequests.doWalletTransferStatus(transferStatusReq,
		// transactionDetails.getMerchantId());
	}

	public void sendFlaggedCallback() {
		walletTransaferAsync.sendFlaggedNotification();
	}
}
