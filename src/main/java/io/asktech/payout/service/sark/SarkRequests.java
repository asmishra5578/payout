package io.asktech.payout.service.sark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.asktech.payout.service.sark.dto.SarkStatusRequest;
import io.asktech.payout.service.sark.dto.SarkStatusResponse;
import io.asktech.payout.service.sark.dto.SarkTransactionResponse;
import io.asktech.payout.service.sark.dto.SarkTrasactionRequest;
import io.asktech.payout.service.sark.dto.SarkUpiTransactionRequest;
import io.asktech.payout.service.sark.dto.SarkUpiTrasactionResponse;
import io.asktech.payout.utils.ApiReqResLogger;
import io.asktech.payout.utils.Utility;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

@Component
public class SarkRequests {

    @Value("${sark.requestUrl}")
    String requestUrl;

    @Value("${sark.statusCheck}")
    String statusCheckUrl;

    @Value("${sark.requestUpiUrl}")
    String requestUrlUpi;
    static Logger logger = LoggerFactory.getLogger(SarkRequests.class);
    @Autowired
    ApiReqResLogger apiReqResLogger;
    // private static final int socketTime = 60000;
    // private static final int connectTime = 90000;

    public SarkTransactionResponse accountTransfer(SarkTrasactionRequest sarkTrasactionRequest,
            String merchantid) throws JsonProcessingException {
        String orderid = sarkTrasactionRequest.getUniqueRefId();
        apiReqResLogger.storeLogs(merchantid, orderid, Utility.convertDTO2JsonString(sarkTrasactionRequest), "SARK",
                "ACCTRANSFER", null, null, "INITIATED");
        logger.info("SARK REQ::" + Utility.convertDTO2JsonString(sarkTrasactionRequest));
        // UnirestInstance unirest = Unirest.primaryInstance();
        // unirest.config().connectTimeout(60000);
        // Unirest.config().reset();
        // Unirest.config()
        // .socketTimeout(socketTime)
        // .connectTimeout(connectTime);
        HttpResponse<SarkTransactionResponse> response = Unirest.post(requestUrl)
                .header("Connection", "keep-alive")
                .header("Content-Type", "application/json")
                .body(sarkTrasactionRequest).asObject(SarkTransactionResponse.class)
                .ifFailure(r -> {
                    logger.error("Oh No! Status" + r.getStatus());
                    r.getParsingError().ifPresent(e -> {
                        logger.error("Parsing Exception: ", e);
                        logger.error("Original body: " + e.getOriginalBody());
                        apiReqResLogger.storeLogs(merchantid, orderid, null, "SARK", "APICHECK",
                                e.getOriginalBody(), "ERROR", "ERROR");

                    });

                });

        apiReqResLogger.storeLogs(merchantid, orderid, Utility.convertDTO2JsonString(sarkTrasactionRequest), "SARK",
                "ACCTRANSFER", Utility.convertDTO2JsonString(response.getBody()), null, "COMPLETE");
        return response.getBody();
    }

    public SarkUpiTrasactionResponse upiTransfer(SarkUpiTransactionRequest sarkTrasactionRequest,
            String merchantid) throws JsonProcessingException {
        String orderid = sarkTrasactionRequest.getUniqueRefId();
        apiReqResLogger.storeLogs(merchantid, orderid, Utility.convertDTO2JsonString(sarkTrasactionRequest), "SARK",
                "UPITRANSFER", null, null, "INITIATED");
        logger.info("SARK REQ::" + Utility.convertDTO2JsonString(sarkTrasactionRequest));
        // UnirestInstance unirest = Unirest.primaryInstance();
        // unirest.config().connectTimeout(60000);
        // Unirest.config().reset();
        // Unirest.config()
        // .socketTimeout(socketTime)
        // .connectTimeout(connectTime);
        HttpResponse<SarkUpiTrasactionResponse> response = Unirest.post(requestUrlUpi)
                .header("Connection", "keep-alive")
                .header("Content-Type", "application/json")
                .body(sarkTrasactionRequest).asObject(SarkUpiTrasactionResponse.class)
                .ifFailure(r -> {
                    logger.error("Oh No! Status" + r.getStatus());
                    r.getParsingError().ifPresent(e -> {
                        logger.error("Parsing Exception: ", e);
                        logger.error("Original body: " + e.getOriginalBody());
                        apiReqResLogger.storeLogs(merchantid, orderid, null, "SARK", "APICHECK",
                                e.getOriginalBody(), "ERROR", "ERROR");

                    });

                });
        apiReqResLogger.storeLogs(merchantid, orderid, Utility.convertDTO2JsonString(sarkTrasactionRequest), "SARK",
                "UPITRANSFER", Utility.convertDTO2JsonString(response.getBody()), null, "COMPLETE");
        return response.getBody();
    }

    public SarkStatusResponse statusRequest(SarkStatusRequest sarkStatusRequest) throws JsonProcessingException {
        // UnirestInstance unirest = Unirest.primaryInstance();
        // unirest.config().connectTimeout(60000);
        // Unirest.config().reset();
        // Unirest.config()
        // .socketTimeout(socketTime)
        // .connectTimeout(connectTime);
        HttpResponse<SarkStatusResponse> response = Unirest.post(statusCheckUrl)
                .header("Connection", "keep-alive")
                .header("Content-Type", "application/json")
                .body(sarkStatusRequest).asObject(SarkStatusResponse.class)
                .ifFailure(Object.class, r -> {
                    Object e = r.getBody();
                    try {
                        logger.info("Contact Response error::" + Utility.convertDTO2JsonString(e));
                    } catch (JsonProcessingException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                });
        return response.getBody();
    }
}
