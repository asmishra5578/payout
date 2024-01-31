package io.asktech.payout.schedular;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.asktech.payout.constants.ErrorValues;
import io.asktech.payout.dto.merchant.AccountTransferMerReq;
import io.asktech.payout.dto.merchant.AccountTransferUPIMerReq;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.modal.merchant.AccountTransactionReq;
import io.asktech.payout.modal.merchant.AccountTransactionUPIReq;
import io.asktech.payout.repository.merchant.AccountTransactionReqRepo;
import io.asktech.payout.repository.merchant.AccountTransactionUPIReqRepo;
import io.asktech.payout.repository.merchant.BankAccountVerifyReqDetailsRepo;
import io.asktech.payout.repository.merchant.TransactionDetailsRepo;
import io.asktech.payout.repository.merchant.WalletTransactionReqRepo;
import io.asktech.payout.service.PayoutMerchant;
import io.asktech.payout.service.PayoutWalletAdminService;
import io.asktech.payout.service.WalletTransaferAsync;
import io.asktech.payout.service.save.MerchantRequestSave;
import io.asktech.payout.utils.Utility;
import io.asktech.payout.wallet.dto.WalletRechargeResDto;
import io.asktech.payout.wallet.modal.BalanceProcessorQueue;
import io.asktech.payout.wallet.repository.BalanceProcessorQueueRepository;

@Service
public class PayoutPaymentAPI implements ErrorValues {
	static Logger logger = LoggerFactory.getLogger(PayoutPaymentAPI.class);

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
	WalletTransaferAsync walletTransaferAsync;
	@Autowired
	PayoutMerchant payoutMerchant;
	@Autowired
	MerchantRequestSave merchantRequestSave;
	@Autowired
	BalanceProcessorQueueRepository balanceProcessorQueueRepository;

	public void balanceDeductionProcessor(int merchantThreadFlag) throws JsonProcessingException, ValidationExceptions {

		List<BalanceProcessorQueue> debitRequest = balanceProcessorQueueRepository
				.findByStatusAndMerchantThreadFlagOrderBySnoAsc("PENDING", merchantThreadFlag);
		// logger.info("DEBIT Request Queue" + Utility.convertDTO2JsonString(debitRequest));
		for (BalanceProcessorQueue balanceProcessorQueue : debitRequest) {

			if (balanceProcessorQueue.getBankaccount() != null) {
				if (balanceProcessorQueue.getTrType().equalsIgnoreCase("DEBIT")) {
					balanceProcessorQueueRepository.save(accountTransaferBalanceDeduction(balanceProcessorQueue));
				} else if (balanceProcessorQueue.getTrType().equalsIgnoreCase("CREDIT")) {

				} else if (balanceProcessorQueue.getTrType().equalsIgnoreCase("REFUND")) {
					logger.info("REFUND DEBIT Request Queue" + Utility.convertDTO2JsonString(balanceProcessorQueue));
					balanceProcessorQueueRepository.save(accountTransaferBalanceRefund(balanceProcessorQueue));
				}

			} else if (balanceProcessorQueue.getUpiVpa() != null) {
				if (balanceProcessorQueue.getTrType().equalsIgnoreCase("DEBIT")) {
					balanceProcessorQueueRepository.save(upiTransaferBalanceDeduction(balanceProcessorQueue));
				} else if (balanceProcessorQueue.getTrType().equalsIgnoreCase("CREDIT")) {

				} else if (balanceProcessorQueue.getTrType().equalsIgnoreCase("REFUND")) {
					balanceProcessorQueueRepository.save(upiTransaferBalanceRefund(balanceProcessorQueue));
				}
			}
		}

	}

	public BalanceProcessorQueue upiTransaferBalanceRefund(BalanceProcessorQueue balanceProcessorQueue)
			throws ValidationExceptions, JsonProcessingException {

		AccountTransferUPIMerReq accountTransferUPIMerReq = new AccountTransferUPIMerReq();
		AccountTransactionUPIReq accountTransactionUPIReq = accountTransactionUPIReqRepo
				.findByInternalOrderId(balanceProcessorQueue.getInternalOrderId());

		accountTransferUPIMerReq.setAmount(accountTransactionUPIReq.getAmount());
		accountTransferUPIMerReq.setRequestType(accountTransactionUPIReq.getRequestType());
		accountTransferUPIMerReq.setInternalOrderid(accountTransactionUPIReq.getInternalOrderId());
		accountTransferUPIMerReq.setRequestType(accountTransactionUPIReq.getRequestType());
		accountTransferUPIMerReq.setBeneficiaryVPA(accountTransactionUPIReq.getBeneficiaryVPA());

		WalletRechargeResDto walletRechargeResDto = payoutMerchant.balanceRefund(accountTransferUPIMerReq,
				balanceProcessorQueue);

		balanceProcessorQueue.setStatus(walletRechargeResDto.getStatus());
		balanceProcessorQueue.setRemarks(walletRechargeResDto.getRemark());
		return balanceProcessorQueue;
	}

	public BalanceProcessorQueue accountTransaferBalanceRefund(BalanceProcessorQueue balanceProcessorQueue)
			throws ValidationExceptions, JsonProcessingException {

		AccountTransferMerReq accountTransferMerReq = new AccountTransferMerReq();
		AccountTransactionReq accountTransactionReq = accountTransactionReqRepo
				.findByInternalOrderId(balanceProcessorQueue.getInternalOrderId());

		accountTransferMerReq.setAmount(accountTransactionReq.getAmount());
		accountTransferMerReq.setRequestType(accountTransactionReq.getRequestType());
		accountTransferMerReq.setInternalOrderid(accountTransactionReq.getInternalOrderId());
		accountTransferMerReq.setRequestType(accountTransactionReq.getRequestType());
		accountTransferMerReq.setBankaccount(accountTransactionReq.getBankaccount());

		WalletRechargeResDto walletRechargeResDto = payoutMerchant.balanceRefund(accountTransferMerReq,
				balanceProcessorQueue);

		balanceProcessorQueue.setStatus(walletRechargeResDto.getStatus());
		balanceProcessorQueue.setRemarks(walletRechargeResDto.getRemark());
		return balanceProcessorQueue;
	}

	public BalanceProcessorQueue upiTransaferBalanceDeduction(BalanceProcessorQueue balanceProcessorQueue)
			throws ValidationExceptions, JsonProcessingException {
		AccountTransactionUPIReq accountTransactionUPIReq = accountTransactionUPIReqRepo
				.findByInternalOrderId(balanceProcessorQueue.getInternalOrderId());

		AccountTransferUPIMerReq dto = new AccountTransferUPIMerReq();
		dto.setAmount(accountTransactionUPIReq.getAmount());
		dto.setInternalOrderid(accountTransactionUPIReq.getInternalOrderId());
		dto.setOrderid(accountTransactionUPIReq.getOrderId());
		dto.setPgName(accountTransactionUPIReq.getPgName());
		dto.setPhonenumber(accountTransactionUPIReq.getPhonenumber());
		dto.setPurpose(accountTransactionUPIReq.getPurpose());
		dto.setRequestType(accountTransactionUPIReq.getRequestType());
		dto.setBeneficiaryVPA(accountTransactionUPIReq.getBeneficiaryVPA());
		dto.setReqStatus("PROCESSING");
		WalletRechargeResDto py = payoutMerchant.balanceDeduction(dto, accountTransactionUPIReq.getMerchantId());

		if (py.getStatus().equals("SUCCESS")) {
			accountTransactionUPIReq.setRequestStatus("BALDEDUCTED");
			accountTransactionUPIReqRepo.save(accountTransactionUPIReq);
			balanceProcessorQueue.setStatus("SUCCESS");
			balanceProcessorQueue.setRemarks("Balance Deducted...");

		} else {

			accountTransactionUPIReq.setRequestStatus("FAILURE");
			accountTransactionUPIReq.setTrRemarks("Low Wallet Balance");
			dto.setReqStatus("FAILURE");
			dto.setTrRemark("Low Wallet Balance");
			accountTransactionUPIReqRepo.save(accountTransactionUPIReq);
			merchantRequestSave.merchantTransactionDetailsUPI(dto, accountTransactionUPIReq.getMerchantId());
			balanceProcessorQueue.setStatus("FAILURE");
			balanceProcessorQueue.setRemarks("Low Wallet Balance");
		}

		return balanceProcessorQueue;

	}

	public BalanceProcessorQueue accountTransaferBalanceDeduction(BalanceProcessorQueue balanceProcessorQueue)
			throws JsonProcessingException, ValidationExceptions {
		AccountTransactionReq accountTransactionReq = accountTransactionReqRepo
				.findByInternalOrderId(balanceProcessorQueue.getInternalOrderId());

		AccountTransferMerReq dto = new AccountTransferMerReq();
		dto.setAmount(accountTransactionReq.getAmount());
		dto.setBankaccount(accountTransactionReq.getBankaccount());
		dto.setBeneficiaryName(accountTransactionReq.getBeneficiaryName());
		dto.setIfsc(accountTransactionReq.getIfsc());
		dto.setInternalOrderid(accountTransactionReq.getInternalOrderId());
		dto.setOrderid(accountTransactionReq.getOrderId());
		dto.setPgName(accountTransactionReq.getPgName());
		dto.setPhonenumber(accountTransactionReq.getPhonenumber());
		dto.setPurpose(accountTransactionReq.getPurpose());
		dto.setRequestType(accountTransactionReq.getRequestType());
		dto.setReqStatus("PROCESSING");

		WalletRechargeResDto py = payoutMerchant.balanceDeduction(dto, accountTransactionReq.getMerchantId());
		logger.info("WalletRechargeResDto::" + Utility.convertDTO2JsonString(py));
		if (py.getStatus().equals("SUCCESS")) {
			accountTransactionReq.setRequestStatus("BALDEDUCTED");
			accountTransactionReqRepo.save(accountTransactionReq);
			balanceProcessorQueue.setStatus("SUCCESS");
			balanceProcessorQueue.setRemarks("Balance Deducted...");
		} else {

			accountTransactionReq.setRequestStatus("FAILURE");
			accountTransactionReq.setTrRemarks("Low Wallet Balance");
			accountTransactionReqRepo.save(accountTransactionReq);
			dto.setReqStatus("FAILURE");
			dto.setTrRemark("Low Wallet Balance");
			merchantRequestSave.MerchantTransactionDetails(dto, accountTransactionReq.getMerchantId());
			balanceProcessorQueue.setStatus("FAILURE");
			balanceProcessorQueue.setRemarks("Low Wallet Balance");
		}

		return balanceProcessorQueue;
	}

	public void accountTransferProcessor(int threadFlag) throws JsonProcessingException, ValidationExceptions {
		List<AccountTransactionReq> accountReq = accountTransactionReqRepo
				.findByRequestStatusAndThreadFlag("BALDEDUCTED", threadFlag);
		// logger.info(Utility.convertDTO2JsonString(accountReq));
		accountReq.forEach(accountRe -> accountRe.setRequestStatus("PROCESSING"));
		accountTransactionReqRepo.saveAll(accountReq);
		//logger.info("\n"+ accountReq.size() +"\n");
		for (AccountTransactionReq ar : accountReq) {

			logger.info("PROCESSING::" + Utility.convertDTO2JsonString(ar));
			String merchantId = ar.getMerchantId();
			AccountTransferMerReq dto = new AccountTransferMerReq();
			dto.setAmount(ar.getAmount());
			dto.setBankaccount(ar.getBankaccount());
			dto.setBeneficiaryName(ar.getBeneficiaryName());
			dto.setIfsc(ar.getIfsc());
			dto.setInternalOrderid(ar.getInternalOrderId());
			dto.setOrderid(ar.getOrderId());
			dto.setPgName(ar.getPgName());
			dto.setPhonenumber(ar.getPhonenumber());
			dto.setPurpose(ar.getPurpose());
			dto.setRequestType(ar.getRequestType());
			dto.setReqStatus("PROCESSING");
			dto.setMerchantThreadFlag(ar.getMerchantThreadFlag());
			walletTransaferAsync.accountTransfer(dto, merchantId, ar);
			try {
				Thread.sleep(1000); // sleep/stop a thread for 1 second
			} catch (InterruptedException e) {
				logger.error("An Excetion occured: " + e.getMessage());
			}
		}
	}

	public void upiTransferProcessor(int threadFlag) throws JsonProcessingException, ValidationExceptions {
		List<AccountTransactionUPIReq> accountReq = accountTransactionUPIReqRepo
				.findByRequestStatusAndThreadFlag("BALDEDUCTED", threadFlag);
		accountReq.forEach(accountRe -> accountRe.setRequestStatus("PROCESSING"));
		accountTransactionUPIReqRepo.saveAll(accountReq);
		for (AccountTransactionUPIReq ar : accountReq) {
			String merchantId = ar.getMerchantId();
			AccountTransferUPIMerReq dto = new AccountTransferUPIMerReq();
			dto.setAmount(ar.getAmount());
			dto.setInternalOrderid(ar.getInternalOrderId());
			dto.setOrderid(ar.getOrderId());
			dto.setPgName(ar.getPgName());
			dto.setPhonenumber(ar.getPhonenumber());
			dto.setPurpose(ar.getPurpose());
			dto.setRequestType(ar.getRequestType());
			dto.setBeneficiaryName(ar.getBeneficiaryName());
			dto.setBeneficiaryVPA(ar.getBeneficiaryVPA());
			dto.setReqStatus("PROCESSING");
			dto.setMerchantThreadFlag(ar.getMerchantThreadFlag());
			walletTransaferAsync.upiTransfer(dto, merchantId, ar);
		}
	}
}
