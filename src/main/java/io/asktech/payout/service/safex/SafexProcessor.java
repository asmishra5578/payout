package io.asktech.payout.service.safex;

import java.text.ParseException;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.asktech.payout.dto.merchant.AccountTransferMerReq;
import io.asktech.payout.dto.merchant.TransactionResponseMerRes;
import io.asktech.payout.dto.reqres.TransferStatusReq;
import io.asktech.payout.dto.reqres.WalletTransferRes;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.modal.merchant.PgDetails;
import io.asktech.payout.modal.merchant.TransactionDetails;
import io.asktech.payout.service.safex.dto.SafexResponseDto;
import io.asktech.payout.service.safex.dto.ServiceRes;
import io.asktech.payout.utils.Utility;

@Service
public class SafexProcessor {
    @Autowired
    SafexRequests safexRequests;
    @Autowired
    SafexUtil safexUtil;

    static Logger logger = LoggerFactory.getLogger(SafexProcessor.class);

    public WalletTransferRes doAccountTransfer(AccountTransferMerReq dto, String merchantid, PgDetails pg)
            throws JsonProcessingException, ParseException, ValidationExceptions {
        WalletTransferRes walletTransferRes = new WalletTransferRes();
        walletTransferRes.setPgId(pg.getPgId());
        String orderid = dto.getInternalOrderid();
        try {
            String SessionKeys = generateRequestKey(pg, merchantid, orderid);
            logger.info(SessionKeys);
            String payload = safexUtil.getFundTransferRequest(dto, SessionKeys, pg);
            ServiceRes serviceRes = safexRequests.sendAccountTransfer(pg, merchantid, payload, orderid);
            SafexResponseDto safexResponseDto = safexUtil.getFundTransferResponse(serviceRes.getPayload(), SessionKeys,
                    orderid);
            walletTransferRes = walletStatusUpdate(walletTransferRes, safexResponseDto);
            return walletTransferRes;
        } catch (Exception e) {
            logger.error("safex fundTransferResponse::" + e.getMessage());
            walletTransferRes.setStatus("FAILURE");
            walletTransferRes.setStatusMessage("Transfer Api ERROR::" + e.getMessage());
            return walletTransferRes;
        }
    }

    public TransactionResponseMerRes doWalletTransferStatus(TransferStatusReq dto, String merchantid,
            TransactionDetails transactionDetails, PgDetails pg) throws JsonProcessingException {
        TransactionResponseMerRes transactionResponseMerRes = new TransactionResponseMerRes();
        try {

            String SessionKeys = generateStatusRequestKey(pg, merchantid, transactionDetails.getInternalOrderId());
            logger.info("SessionKey::" + SessionKeys);
            String payload = safexUtil.getStatusAPIRequest(transactionDetails.getInternalOrderId(), SessionKeys, pg);
            ServiceRes serviceRes = safexRequests.getTransferStatus(pg, merchantid, payload,
                    transactionDetails.getInternalOrderId());
            SafexResponseDto safexResponseDto = safexUtil.getStatusAPIResponse(serviceRes.getPayload(), SessionKeys,
                    transactionDetails.getInternalOrderId());
            logger.info("Safex Status ResponseDetails::" + Utility.convertDTO2JsonString(safexResponseDto));

            if (safexResponseDto != null) {
                if (safexResponseDto.getPayOutBean().getTxnStatus() == null) {
                    transactionResponseMerRes.setMessage(transactionDetails.getTransactionMessage());
                    transactionResponseMerRes.setStatus(transactionDetails.getTransactionStatus());
                    transactionResponseMerRes.setAmount(transactionDetails.getAmount());
                    transactionResponseMerRes.setOrderid(transactionDetails.getOrderId());
                    return transactionResponseMerRes;
                }
                transactionResponseMerRes.setMessage(safexResponseDto.getPayOutBean().getStatusDesc());
                transactionResponseMerRes
                        .setStatus(statusCheckResponse(safexResponseDto.getPayOutBean().getTxnStatus()));
                transactionResponseMerRes.setStatusMessage(safexResponseDto.getPayOutBean().getStatusDesc());
                transactionResponseMerRes.setUtrId(safexResponseDto.getPayOutBean().getBankRefNo());
            } else {
                transactionResponseMerRes.setStatus("FAILURE");
                transactionResponseMerRes.setMessage("Transaction Incomplete");
            }
            transactionResponseMerRes.setAmount(transactionDetails.getAmount());
            transactionResponseMerRes.setOrderid(transactionDetails.getOrderId());

        } catch (Exception e) {
            transactionResponseMerRes.setMessage(transactionDetails.getTransactionMessage());
            transactionResponseMerRes.setStatus(transactionDetails.getTransactionStatus());
            transactionResponseMerRes.setAmount(transactionDetails.getAmount());
            transactionResponseMerRes.setOrderid(transactionDetails.getOrderId());
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return transactionResponseMerRes;
    }

    public String generateRequestKey(PgDetails pgDetails, String merchantid, String orderid)
            throws JsonProcessingException, ParseException, ValidationExceptions {
        // orderid = "SES" + orderid;
        String sessionReq = safexUtil.getApiSessionKey(pgDetails, orderid);
        ServiceRes responseSafex = safexRequests.getSessionKey(pgDetails, merchantid, sessionReq, orderid);
        responseSafex.getPayload();
        logger.info("generateRequestKey::" + Utility.convertDTO2JsonString(responseSafex));
        return safexUtil.getApiSessionKey(responseSafex, pgDetails, orderid);
    }

    public String generateStatusRequestKey(PgDetails pgDetails, String merchantid, String orderid)
            throws JsonProcessingException, ParseException, ValidationExceptions {
        // orderid = "SES" + orderid;
        String sessionReq = safexUtil.getStatusApiSessionKey(pgDetails, orderid);
        ServiceRes responseSafex = safexRequests.getSessionKey(pgDetails, merchantid, sessionReq, orderid);
        logger.info("generateStatusRequestKey::" + Utility.convertDTO2JsonString(responseSafex));
        return safexUtil.getApiSessionKey(responseSafex, pgDetails, orderid);
    }

    private WalletTransferRes walletStatusUpdate(WalletTransferRes walletTransferRes, SafexResponseDto res) {
        if (res.getPayOutBean().getStatusCode() == null) {
            walletTransferRes.setStatus("FAILURE");
            walletTransferRes.setStatusMessage(res.getResponse().getCode() + "|" + res.getResponse().getDescription());
            walletTransferRes.setReferenceId(res.getHeader().getRequestId());
            return walletTransferRes;
        }
        if (res.getPayOutBean().getStatusCode().equals("0000")) {
            walletTransferRes.setStatus("SUCCESS");
            walletTransferRes.setStatusMessage(res.getPayOutBean().getStatusDesc());
            walletTransferRes.setReferenceId(res.getPayOutBean().getPayoutId());
            // walletTransferRes.setUtr(res.getData().getUtr());
        } else {
            if (res.getPayOutBean().getTxnStatus() != null) {
                walletTransferRes.setStatus(statusCheckResponse(res.getPayOutBean().getTxnStatus()));
                walletTransferRes.setStatusMessage(res.getPayOutBean().getStatusDesc());
                walletTransferRes.setReferenceId(res.getPayOutBean().getPayoutId());
            } else {
                walletTransferRes.setStatus("FAILURE");
                walletTransferRes.setStatusMessage(res.getResponse().getDescription());
                walletTransferRes.setReferenceId(res.getHeader().getRequestId());

            }
        }
        return walletTransferRes;
    }

    private String statusCheckResponse(String status) {
        if (status.equalsIgnoreCase("FAILED")) {
            status = "FAILURE";
        }
        if (status.equalsIgnoreCase("QUEUED")) {
            status = "PENDING";
        }
        return status;

    }
}
