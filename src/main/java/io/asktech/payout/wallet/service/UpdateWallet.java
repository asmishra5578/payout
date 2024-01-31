package io.asktech.payout.wallet.service;

import java.text.DecimalFormat;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import io.asktech.payout.wallet.dto.TrDetails;
import io.asktech.payout.wallet.dto.WalletRechargeReqDto;
import io.asktech.payout.wallet.dto.WalletReversalReqDto;
import io.asktech.payout.wallet.modal.WalletTransactions;
import io.asktech.payout.wallet.repository.WalletTransactionsRepo;

@Component
public class UpdateWallet {
	@Autowired
	WalletTransactionsRepo walletTransactionsRepo;
	static Logger logger = LoggerFactory.getLogger(UpdateWallet.class);
	
	@Async
	public void insertTrasactionDetails(WalletRechargeReqDto walletRechargeReqDto, String merchantId,
			TrDetails trDetails) {
		logger.info("insertTrasactionDetails - ASYNC");
		DecimalFormat df = new DecimalFormat("#.##");
		WalletTransactions walletTransactions = new WalletTransactions();
		walletTransactions.setOpeningBalance(trDetails.getOpeningBalance());
		walletTransactions.setTransactionType(walletRechargeReqDto.getTransactionType());
		walletTransactions.setAmount(String.valueOf(df.format(Double.parseDouble(walletRechargeReqDto.getAmount()))));
		walletTransactions.setClosingBalance(trDetails.getClosingBalance());
		walletTransactions.setCredit_debit(trDetails.getCreditDebit());
		String trid = UUID.randomUUID().toString().replace("-", "");
		walletTransactions.setTransactionId(trid);
		walletTransactions.setWalletid(trDetails.getWalletId());
		walletTransactions.setRemarks(walletRechargeReqDto.getRemarks());
		walletTransactions.setPurpose(walletRechargeReqDto.getPurpose());
		walletTransactions.setTransactionStatus(trDetails.getTransactionStatus());
		walletTransactions.setStatusRemarks(trDetails.getStatusRemarks());
		walletTransactions.setMerchantId(merchantId);
		walletTransactions.setMainWalletId(trDetails.getMainWalletId());
		if (trDetails.getReferenceId() != null) {
			walletTransactions.setReferenceId(trDetails.getReferenceId());
		}else{
			walletTransactions.setReferenceId(trid);
		}
		walletTransactionsRepo.save(walletTransactions);

	}
	
	@Async
	public void insertTrasactionDetails(WalletReversalReqDto walletRechargeReqDto, String merchantId,
			TrDetails trDetails) {
		logger.info("insertTrasactionDetails - ASYNC2");
		DecimalFormat df = new DecimalFormat("#.##");
		WalletTransactions walletTransactions = new WalletTransactions();
		walletTransactions.setOpeningBalance(trDetails.getOpeningBalance());
		walletTransactions.setTransactionType(walletRechargeReqDto.getTransactionType());
		walletTransactions.setAmount(String.valueOf(df.format(Double.parseDouble(walletRechargeReqDto.getAmount()))));
		walletTransactions.setClosingBalance(trDetails.getClosingBalance());
		walletTransactions.setCredit_debit(trDetails.getCreditDebit());
		String trid = UUID.randomUUID().toString().replace("-", "");
		walletTransactions.setTransactionId(trid);
		walletTransactions.setWalletid(trDetails.getWalletId());
		walletTransactions.setRemarks(walletRechargeReqDto.getRemarks());
		walletTransactions.setPurpose(walletRechargeReqDto.getPurpose());
		walletTransactions.setTransactionStatus(trDetails.getTransactionStatus());
		walletTransactions.setStatusRemarks(trDetails.getStatusRemarks());
		walletTransactions.setMerchantId(merchantId);
		walletTransactions.setMainWalletId(trDetails.getMainWalletId());
		if (trDetails.getReferenceId() != null) {
			walletTransactions.setReferenceId(trDetails.getReferenceId());
		}else{
			walletTransactions.setReferenceId(trid);
		}
		walletTransactionsRepo.save(walletTransactions);

	}
}
