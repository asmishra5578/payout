package io.asktech.payout.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.asktech.payout.dto.merchant.AccountTransferMerReq;
import io.asktech.payout.dto.merchant.AccountTransferUPIMerReq;
import io.asktech.payout.dto.merchant.TransactionResponseMerRes;
import io.asktech.payout.dto.reqres.TransferStatusReq;
import io.asktech.payout.dto.reqres.WalletTransferRes;
import io.asktech.payout.modal.merchant.PgDetails;
import io.asktech.payout.modal.merchant.TransactionDetails;

public interface IProcessor {
    WalletTransferRes doAccountTransfer(AccountTransferMerReq dto, String merchantid, PgDetails pg) throws JsonProcessingException;
    TransactionResponseMerRes doWalletTransferStatus(TransferStatusReq dto, String merchantid, TransactionDetails transactionDetails, PgDetails pg) throws JsonProcessingException;
    WalletTransferRes doAccountTransferUPI(AccountTransferUPIMerReq dto, String merchantid, PgDetails pg) throws JsonProcessingException;
}
