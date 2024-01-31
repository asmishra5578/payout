package io.asktech.payout.service.cashfree.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.asktech.payout.dto.merchant.TransactionResponseMerRes;
import io.asktech.payout.dto.reqres.TransferStatusReq;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.modal.merchant.TransactionDetails;
import io.asktech.payout.repository.merchant.TransactionDetailsRepo;
import io.asktech.payout.service.PayoutWalletAdminService;

@Component
public class CashfreeAsyncStatus {

	@Autowired
	PayoutWalletAdminService payoutWalletAdminService;
	@Autowired
	TransactionDetailsRepo transactionDetailsRepository;

	@Async
	public void updateStatus(String orderId, TransactionResponseMerRes wallettransferRes) {
		TransactionDetails tr = transactionDetailsRepository.findByOrderId(orderId);
		if (tr != null) {
			if (!tr.getTransactionStatus().equalsIgnoreCase(wallettransferRes.getStatus())) {
				tr.setTransactionStatus(wallettransferRes.getStatus());
				tr.setTransactionMessage(wallettransferRes.getStatusMessage());
				if(wallettransferRes.getUtrId() != null){
					tr.setUtrid(wallettransferRes.getUtrId());
				}
				transactionDetailsRepository.save(tr);
			}
		}
	}

	@Async
	public void runStatusAPI(TransferStatusReq dto, String merchantid)
			throws JsonProcessingException, ValidationExceptions {
		payoutWalletAdminService.getWalletTransferStatus(dto, merchantid);
	}
}
