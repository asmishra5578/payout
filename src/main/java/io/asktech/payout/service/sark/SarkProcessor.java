package io.asktech.payout.service.sark;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.asktech.payout.dto.merchant.AccountTransferMerReq;
import io.asktech.payout.dto.merchant.AccountTransferUPIMerReq;
import io.asktech.payout.dto.merchant.TransactionResponseMerRes;
import io.asktech.payout.dto.reqres.TransferStatusReq;
import io.asktech.payout.dto.reqres.WalletTransferRes;
import io.asktech.payout.modal.merchant.PgDetails;
import io.asktech.payout.modal.merchant.TransactionDetails;
import io.asktech.payout.service.IProcessor;
import io.asktech.payout.service.sark.dto.SarkStatusRequest;
import io.asktech.payout.service.sark.dto.SarkStatusResponse;
import io.asktech.payout.service.sark.dto.SarkTransactionResponse;
import io.asktech.payout.service.sark.dto.SarkTrasactionRequest;
import io.asktech.payout.service.sark.dto.SarkUpiTransactionRequest;
import io.asktech.payout.service.sark.dto.SarkUpiTrasactionResponse;
import io.asktech.payout.utils.Utility;

@Service
public class SarkProcessor implements IProcessor {
    @Autowired
    SarkRequests sarkRequests;
    @Autowired
    SarkUtil sarkUtil;
    @Autowired
    SaveSarkDetails saveSarkDetails;
    static Logger logger = LoggerFactory.getLogger(SarkProcessor.class);

    @Override
    public WalletTransferRes doAccountTransfer(AccountTransferMerReq dto, String merchantid, PgDetails pg) {
        WalletTransferRes walletTransferRes = new WalletTransferRes();
        walletTransferRes.setPgId(pg.getPgId());
        try {
            SarkTrasactionRequest sarkTrasactionRequest = sarkUtil.generateTransferRequest(dto, merchantid, pg);
            saveSarkDetails.storeReqRes(sarkTrasactionRequest, null, dto.getInternalOrderid());
            SarkTransactionResponse sarkTransactionResponse = sarkRequests.accountTransfer(sarkTrasactionRequest,
                    merchantid);
            saveSarkDetails.storeReqRes(null, sarkTransactionResponse, dto.getInternalOrderid());
            walletTransferRes = walletStatusUpdate(walletTransferRes, sarkTransactionResponse);
        } catch (Exception e) {
            logger.error("Sark fundTransferResponse::" + e.getMessage());
            walletTransferRes.setStatus("PENDING");
            walletTransferRes.setStatusMessage("PENDING");
            walletTransferRes.setErrorMsg(e.getMessage());
            e.printStackTrace();
        }
        return walletTransferRes;
    }


    @Override
    public WalletTransferRes doAccountTransferUPI(AccountTransferUPIMerReq dto, String merchantid, PgDetails pg) {
        WalletTransferRes walletTransferRes = new WalletTransferRes();
        walletTransferRes.setPgId(pg.getPgId());
        try {
            SarkUpiTransactionRequest sarkTrasactionRequest = sarkUtil.generateUpiTransferRequest(dto, merchantid, pg);
            saveSarkDetails.storeReqRes(sarkTrasactionRequest, null, dto.getInternalOrderid());
            SarkUpiTrasactionResponse sarkTransactionResponse = sarkRequests.upiTransfer(sarkTrasactionRequest,
                    merchantid);
            saveSarkDetails.storeReqRes(null, sarkTransactionResponse, dto.getInternalOrderid());
            walletTransferRes = walletStatusUpdate(walletTransferRes, sarkTransactionResponse);
        } catch (Exception e) {
            logger.error("Sark fundTransferResponse::" + e.getMessage());
            walletTransferRes.setStatus("PENDING");
            walletTransferRes.setStatusMessage("PENDING");
            walletTransferRes.setErrorMsg(e.getMessage());
            e.printStackTrace();
        }
        return walletTransferRes;
    }


    @Override
    public TransactionResponseMerRes doWalletTransferStatus(TransferStatusReq dto, String merchantid,
            TransactionDetails transactionDetails, PgDetails pg) {
        TransactionResponseMerRes transactionResponseMerRes = new TransactionResponseMerRes();
        try {
            String uniquid = saveSarkDetails.getUniqueId(transactionDetails.getInternalOrderId());
            if (uniquid == null) {
                logger.info("unique id is null");
                transactionResponseMerRes.setMessage(transactionDetails.getTransactionMessage());
                transactionResponseMerRes.setStatus(transactionDetails.getTransactionStatus());
                transactionResponseMerRes.setAmount(transactionDetails.getAmount());
                transactionResponseMerRes.setOrderid(transactionDetails.getOrderId());
                if (transactionDetails.getUtrid() != null) {
                    transactionResponseMerRes.setUtrId(transactionDetails.getUtrid());
                }
                // logger.error(e.getMessage());
                return transactionResponseMerRes;
            }
            SarkStatusRequest sarkStatusRequest = sarkUtil.generateStatusRequest(transactionDetails, merchantid, pg,
                    uniquid);
            SarkStatusResponse sarkStatusResponse = sarkRequests.statusRequest(sarkStatusRequest);
            logger.info("sarkStatusResponse::" + Utility.convertDTO2JsonString(sarkStatusResponse));
            saveSarkDetails.statusUpdate(sarkStatusResponse, transactionDetails.getInternalOrderId());
            if (sarkStatusResponse != null) {
                transactionResponseMerRes = responseStatusUpdate(transactionResponseMerRes, sarkStatusResponse);
            }
            logger.info("transactionResponseMerRes::" + Utility.convertDTO2JsonString(transactionResponseMerRes));
            if (sarkStatusResponse.getStatusDescription() != null) {
                transactionResponseMerRes.setMessage(sarkStatusResponse.getStatusDescription());
            }
            logger.info("transactionResponseMerRes::" + Utility.convertDTO2JsonString(transactionResponseMerRes));
            transactionResponseMerRes.setAmount(transactionDetails.getAmount());
            if (sarkStatusResponse.getRefId() != null) {
                transactionResponseMerRes.setUtrId(sarkStatusResponse.getRefId());
            } else {
                transactionResponseMerRes.setStatus("PENDING");
            }
            logger.info("transactionResponseMerRes::" + Utility.convertDTO2JsonString(transactionResponseMerRes));
            transactionResponseMerRes.setOrderid(transactionDetails.getOrderId());
            logger.info("sarkStatusResponse::" + Utility.convertDTO2JsonString(sarkStatusResponse));
        } catch (Exception e) {
            transactionResponseMerRes.setMessage(transactionDetails.getTransactionMessage());
            transactionResponseMerRes.setStatus(transactionDetails.getTransactionStatus());
            transactionResponseMerRes.setAmount(transactionDetails.getAmount());
            transactionResponseMerRes.setOrderid(transactionDetails.getOrderId());
            if (transactionDetails.getUtrid() != null) {
                transactionResponseMerRes.setUtrId(transactionDetails.getUtrid());
            }
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return transactionResponseMerRes;
    }

    // {"txnTime":"02-06-2022
    // 20:13:54","message":"SUCCESS","status":"1","txnId":"851903","bankRef":"215320719113"}
    
    private WalletTransferRes walletStatusUpdate(WalletTransferRes walletTransferRes, SarkTransactionResponse res) {
        final String failedString = "8001,1344,1346,3431,3426,1327,3432,3326,3372,3301";
        if (res.getStatus().equals("1")) {
            if (res.getBankRef().length() > 5) {
                walletTransferRes.setStatus("SUCCESS");
                walletTransferRes.setUtr(res.getBankRef());
            } else {
                walletTransferRes.setStatus("PENDING");
            }
            walletTransferRes.setStatusMessage(res.getMessage());
            walletTransferRes.setReferenceId(res.getTxnId());
            return walletTransferRes;
        } else if (failedString.contains(res.getStatus())) {
            walletTransferRes.setStatus("PENDING");
            walletTransferRes.setStatusMessage(res.getMessage());
            return walletTransferRes;
        } else {
            walletTransferRes.setStatus("PENDING");
            walletTransferRes.setStatusMessage(res.getMessage());
            walletTransferRes.setReferenceId(res.getTxnId());
            walletTransferRes.setUtr(res.getBankRef());
            return walletTransferRes;
        }

    }


    private WalletTransferRes walletStatusUpdate(WalletTransferRes walletTransferRes, SarkUpiTrasactionResponse res) {
        final String failedString = "8001,1344,1346,3431,3426,1327,3432,3326,3372,3301";
        if (res.getStatus().equals("1")) {
            if (res.getTransaction_details().getBankRef() == null) {
                walletTransferRes.setStatus("PENDING");
            } else {
                if (res.getTransaction_details().getBankRef().length() > 5) {
                    walletTransferRes.setStatus("SUCCESS");
                    if (res.getTransaction_details() != null) {
                        walletTransferRes.setUtr(res.getTransaction_details().getBankRef());
                    }
                } else {
                    walletTransferRes.setStatus("PENDING");
                }
            }
            walletTransferRes.setStatusMessage(res.getMessage());
            if (res.getTransaction_details() != null) {
                walletTransferRes.setReferenceId(res.getTransaction_details().getTxnId());
            }
            return walletTransferRes;
        } else if (failedString.contains(res.getStatus())) {
            walletTransferRes.setStatus("PENDING");
            walletTransferRes.setStatusMessage(res.getMessage());
            return walletTransferRes;
        } else {
            walletTransferRes.setStatus("PENDING");
            walletTransferRes.setStatusMessage(res.getMessage());
            if (res.getTransaction_details() != null) {
                walletTransferRes.setReferenceId(res.getTransaction_details().getTxnId());
                walletTransferRes.setUtr(res.getTransaction_details().getBankRef());
            }
            return walletTransferRes;
        }

    }


    // {"number":"40850100015511","statusDescription":"SUCCESS","amount":"190","txnTime":"2022-06-02
    // 20:13:54","refId":"215320719113","message":"SUCCESS","status":"1","txnId":"851903"}
    private TransactionResponseMerRes responseStatusUpdate(TransactionResponseMerRes walletTransferRes,
            SarkStatusResponse res) {
        final String pendingString[] = { "3", "8", "9", "10", "11", "14", "23", "29", "32", "INITIATED" };
        final String failureString[] = { "2", "5", "4404", "8001", "1344","3301" };
        if (res.getStatus().equals("1103")) {
            return walletTransferRes;
        }
        if (res.getStatus().equals("1") || res.getStatus().equals("24")) {
            if (res.getRefId() == null) {
                walletTransferRes.setStatus("PENDING");
                return walletTransferRes;
            } else {
                if (res.getRefId().length() > 5) {
                    walletTransferRes.setStatus("SUCCESS");
                    walletTransferRes.setUtrId(res.getRefId());
                } else {
                    walletTransferRes.setStatus("PENDING");
                }
            }
            return walletTransferRes;
        } else if (Arrays.stream(pendingString).anyMatch(res.getStatus()::equals)) {
            walletTransferRes.setStatus("PENDING");
            if (res.getRefId().length() > 1) {
                walletTransferRes.setUtrId(res.getRefId());
            }
            return walletTransferRes;
        } else if (Arrays.stream(failureString).anyMatch(res.getStatus()::equals)) {
            walletTransferRes.setStatus("FAILURE");
            return walletTransferRes;
        } else {
            walletTransferRes.setStatus("PENDING");
            return walletTransferRes;
        }

    }

}
