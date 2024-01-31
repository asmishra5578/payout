package io.asktech.payout.service.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.asktech.payout.dto.admin.TransactionReversalRequest;
import io.asktech.payout.dto.admin.TransactionReversalResponse;
import io.asktech.payout.modal.merchant.TransactionDetails;
import io.asktech.payout.repository.merchant.TransactionDetailsRepo;
import io.asktech.payout.service.payoutUtils.PaymentUtils;
import io.asktech.payout.wallet.modal.WalletTransactions;
import io.asktech.payout.wallet.repository.WalletTransactionsRepo;

@Service
public class TransactionManagement {

    @Autowired
    PaymentUtils paymentUtils;
    @Autowired
    TransactionDetailsRepo transactionDetailsRepo;
    @Autowired
    WalletTransactionsRepo walletTransactionsRepo;

    public TransactionReversalResponse transactionReversal(TransactionReversalRequest dto)
            throws JsonProcessingException {
        TransactionDetails transactionDetails = transactionDetailsRepo.findByInternalOrderId(dto.getOrderId());

        if (transactionDetails == null) {
            return new TransactionReversalResponse(dto.getOrderId(), "FAILED", "OrderId Not Found");
        } else {
            if (transactionDetails.getTransactionStatus().equals("REVERSED")) {
                return new TransactionReversalResponse(dto.getOrderId(), "FAILED", "Already Reversed");
            } else {
                List<WalletTransactions> trxs = walletTransactionsRepo.getByReferenceIdandCreditdebit(dto.getOrderId(),
                        "CREDIT");
                if (trxs.size() == 0) {
                    paymentUtils.doManualReversal(transactionDetails);
                    return new TransactionReversalResponse(dto.getOrderId(), "QUEUED", "Reversal Queued");
                } else {
                    return new TransactionReversalResponse(dto.getOrderId(), "FAILED", "Already Reversed in Wallet");
                }
            }
        }
    }
}
