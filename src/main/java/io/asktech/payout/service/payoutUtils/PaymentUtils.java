package io.asktech.payout.service.payoutUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.asktech.payout.modal.merchant.TransactionChangeLogs;
import io.asktech.payout.modal.merchant.TransactionDetails;
import io.asktech.payout.repository.merchant.TransactionChangeLogsRepo;
import io.asktech.payout.repository.merchant.TransactionDetailsRepo;
import io.asktech.payout.service.NotiFyURLService2Merchant;
import io.asktech.payout.service.PayoutMerchant;
import io.asktech.payout.utils.Utility;
import io.asktech.payout.wallet.modal.WalletDetails;
import io.asktech.payout.wallet.repository.WalletDetailsRepo;

@Component
public class PaymentUtils {
    static Logger logger = LoggerFactory.getLogger(PaymentUtils.class);

    public String accountValidation(String merchantid) {
        return null;
    }

    @Autowired
    NotiFyURLService2Merchant notiFyURLService2Merchant;
    @Autowired
    TransactionChangeLogsRepo transactionChangeLogsRepo;

    @Async
    public void sendNotification(TransactionDetails transactionDetails) throws JsonProcessingException {
        TransactionChangeLogs transactionChangeLogs = new TransactionChangeLogs();
        transactionChangeLogs.setMerchantid(transactionDetails.getMerchantId());
        transactionChangeLogs.setOrderId(transactionDetails.getInternalOrderId());
        transactionChangeLogs.setPgName(transactionDetails.getPgname());
        transactionChangeLogs.setStatus(transactionDetails.getTransactionStatus());
        transactionChangeLogs.setStatusMsg(transactionDetails.getTransactionMessage());
        transactionChangeLogs.setStatusResponse(Utility.convertDTO2JsonString(transactionDetails));
        transactionChangeLogsRepo.save(transactionChangeLogs);
        if (transactionDetails.getCallBackURL() != null) {
            if (!transactionDetails.getTransactionStatus().equalsIgnoreCase("ERROR")) {
                notiFyURLService2Merchant.sendNotifyDetails2Merchant(transactionDetails);
            }
        }
    }

    @Lazy
    @Autowired
    PayoutMerchant payoutMerchant;
    @Autowired
    TransactionDetailsRepo transactionDetailsRepo;
    @Autowired
    WalletDetailsRepo walletDetailsRepo;

    @Async
    public void doReversal(TransactionDetails transactionDetails) throws JsonProcessingException {
        if (!transactionDetails.getTransactionMessage().contains("REVERSE")) {
            WalletDetails walletDetails = walletDetailsRepo.findByMerchantid(transactionDetails.getMerchantId());
            if (walletDetails.getInstantReversal() != null) {
                if (walletDetails.getInstantReversal().equals("TRUE")) {
                    payoutMerchant.populateBalanceProcessRefund(transactionDetails,
                            walletDetails.getMerchantThreadFlag());
                    transactionDetails
                            .setTransactionMessage(transactionDetails.getTransactionMessage() + " " + "REVERSED");
                    transactionDetails.setTransactionStatus("REVERSED");
                    transactionDetailsRepo.save(transactionDetails);
                } else {
                    logger.info("Reversal Not allowed::" + Utility.convertDTO2JsonString(transactionDetails));
                }
            } else {
                logger.info("Reversal Not allowed::" + Utility.convertDTO2JsonString(transactionDetails));
            }

        }
    }

    @Async
    public void doManualReversal(TransactionDetails transactionDetails) throws JsonProcessingException {
        logger.info("doManualReversal::"+Utility.convertDTO2JsonString(transactionDetails));
        if (!transactionDetails.getTransactionStatus().contains("REVERSE")) {
            logger.info("doManualReversal Attempt");
            WalletDetails walletDetails = walletDetailsRepo.findByMerchantid(transactionDetails.getMerchantId());
            payoutMerchant.populateBalanceProcessRefund(transactionDetails,
                    walletDetails.getMerchantThreadFlag());
            logger.info("WALLET DETAILS::"+Utility.convertDTO2JsonString(walletDetails));
            transactionDetails
                    .setTransactionMessage(transactionDetails.getTransactionMessage() + " " + "REVERSED");
            transactionDetails.setTransactionStatus("REVERSED");
            transactionDetails.setCallBackFlag("true");
            transactionDetailsRepo.save(transactionDetails);

        }
    }

}
