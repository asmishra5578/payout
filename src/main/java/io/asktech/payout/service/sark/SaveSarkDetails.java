package io.asktech.payout.service.sark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.asktech.payout.service.sark.dto.SarkStatusResponse;
import io.asktech.payout.service.sark.dto.SarkTransactionResponse;
import io.asktech.payout.service.sark.dto.SarkTrasactionRequest;
import io.asktech.payout.service.sark.dto.SarkUpiTransactionRequest;
import io.asktech.payout.service.sark.dto.SarkUpiTrasactionResponse;
import io.asktech.payout.service.sark.models.SarkReqRes;
import io.asktech.payout.service.sark.repo.SarkReqResRepo;
import io.asktech.payout.utils.Utility;

@Component
public class SaveSarkDetails {
    @Autowired
    SarkReqResRepo sarkReqResRepo;
    static Logger logger = LoggerFactory.getLogger(SaveSarkDetails.class);

    @Async
    public void storeReqRes(SarkTrasactionRequest sarkTrasactionRequest,
            SarkTransactionResponse sarkTransactionResponse, String orderId) throws JsonProcessingException {
        SarkReqRes sarkReqRes = null;

        if (sarkTrasactionRequest != null) {
            logger.info("sarkTrasactionRequest::" + Utility.convertDTO2JsonString(sarkTrasactionRequest));
            sarkReqRes = sarkReqResRepo.findByOrderId(orderId);
            if (sarkReqRes == null) {
                sarkReqRes = new SarkReqRes();
                sarkReqRes.setOrderId(orderId);
            }

            sarkReqRes.setReqString(Utility.convertDTO2JsonString(sarkTrasactionRequest));
            sarkReqRes.setStatus("INITIATED");
            sarkReqRes.setUniqueRefId(sarkTrasactionRequest.getUniqueRefId());
        }
        if (sarkTransactionResponse != null) {
            logger.info("sarkTransactionResponse::" + Utility.convertDTO2JsonString(sarkTransactionResponse));
            sarkReqRes = sarkReqResRepo.findByOrderId(orderId);
            if (sarkReqRes == null) {
                sarkReqRes = new SarkReqRes();
                sarkReqRes.setOrderId(orderId);
            }
            sarkReqRes.setResString(Utility.convertDTO2JsonString(sarkTransactionResponse));
            sarkReqRes.setBankRef(sarkTransactionResponse.getBankRef());
            sarkReqRes.setMessage(sarkTransactionResponse.getMessage());
            sarkReqRes.setStatus(sarkTransactionResponse.getStatus());
            sarkReqRes.setTxnTime(sarkTransactionResponse.getTxnTime());
            sarkReqRes.setTxnId(sarkTransactionResponse.getTxnId());
        }
        sarkReqResRepo.save(sarkReqRes);
    }

    @Async
    public void statusUpdate(SarkStatusResponse SarkStatusResponse, String orderId) throws JsonProcessingException {
        SarkReqRes sarkReqRes = null;
        if (SarkStatusResponse != null) {
            logger.info("SarkStatusResponse::" + Utility.convertDTO2JsonString(SarkStatusResponse));
            sarkReqRes = sarkReqResRepo.findByOrderId(orderId);
            if (sarkReqRes == null) {
                sarkReqRes = new SarkReqRes();
                sarkReqRes.setOrderId(orderId);
            }
            sarkReqRes.setStatusString(Utility.convertDTO2JsonString(SarkStatusResponse));
            sarkReqRes.setStatus(SarkStatusResponse.getStatus());
            sarkReqResRepo.save(sarkReqRes);
        }
    }

    @Async
    public void storeReqRes(SarkUpiTransactionRequest sarkTrasactionRequest,
            SarkUpiTrasactionResponse sarkTransactionResponse, String orderId) throws JsonProcessingException {
        SarkReqRes sarkReqRes = null;

        if (sarkTrasactionRequest != null) {
            logger.info("sarkTrasactionRequest::" + Utility.convertDTO2JsonString(sarkTrasactionRequest));
            sarkReqRes = sarkReqResRepo.findByOrderId(orderId);
            if (sarkReqRes == null) {
                sarkReqRes = new SarkReqRes();
                sarkReqRes.setOrderId(orderId);
            }

            sarkReqRes.setReqString(Utility.convertDTO2JsonString(sarkTrasactionRequest));
            sarkReqRes.setStatus("INITIATED");
            sarkReqRes.setUniqueRefId(sarkTrasactionRequest.getUniqueRefId());
        }
        if (sarkTransactionResponse != null) {
            logger.info("sarkTransactionResponse::" + Utility.convertDTO2JsonString(sarkTransactionResponse));
            sarkReqRes = sarkReqResRepo.findByOrderId(orderId);
            if (sarkReqRes == null) {
                sarkReqRes = new SarkReqRes();
                sarkReqRes.setOrderId(orderId);
            }
            sarkReqRes.setResString(Utility.convertDTO2JsonString(sarkTransactionResponse));
            sarkReqRes.setMessage(sarkTransactionResponse.getMessage());
            sarkReqRes.setStatus(sarkTransactionResponse.getStatus());
            if (sarkTransactionResponse.getTransaction_details() != null) {
                sarkReqRes.setBankRef(sarkTransactionResponse.getTransaction_details().getBankRef());
                sarkReqRes.setTxnTime(sarkTransactionResponse.getTransaction_details().getTxnTime());
                sarkReqRes.setTxnId(sarkTransactionResponse.getTransaction_details().getTxnId());
            }
        }
        sarkReqResRepo.save(sarkReqRes);
    }

    public String getUniqueId(String orderId) {
        SarkReqRes sarkReqRes = sarkReqResRepo.findByOrderId(orderId);
        if (sarkReqRes != null) {
            return sarkReqRes.getUniqueRefId();
        }
        return null;
    }
}
