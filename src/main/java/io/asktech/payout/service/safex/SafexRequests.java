package io.asktech.payout.service.safex;

import java.text.ParseException;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.asktech.payout.modal.merchant.PgDetails;
import io.asktech.payout.service.safex.dto.ServiceReqRes;
import io.asktech.payout.service.safex.dto.ServiceRes;
import io.asktech.payout.utils.ApiReqResLogger;
import io.asktech.payout.utils.Utility;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

@Component
public class SafexRequests {

    static Logger logger = LoggerFactory.getLogger(SafexRequests.class);
    @Autowired
    ApiReqResLogger apiReqResLogger;
    @Autowired
    SafexUtil safexUtil;
    @Value("${safex.requestUrl}")
    String requestUrl;

    public ServiceRes getSessionKey(PgDetails pgDetails, String merchantid, String sessionReq, String orderid)
            throws JsonProcessingException, ParseException {

        apiReqResLogger.storeLogs(merchantid, orderid, sessionReq, "SAFEX", "APICHECK", null, null, "INITIATED");
        HttpResponse<ServiceRes> response = Unirest.post(requestUrl)
                .header("Connection", "keep-alive")
                .header("Content-Type", "application/json")
                .body(sessionReq).asObject(ServiceRes.class)
                .ifFailure(Object.class, r -> {
                    Object e = r.getBody();
                    try {
                        logger.info("Contact Response error::" + Utility.convertDTO2JsonString(e));
                        apiReqResLogger.storeLogs(merchantid, orderid, sessionReq, "SAFEX", "APICHECK",
                                Utility.convertDTO2JsonString(e), "ERROR", "ERROR");
                    } catch (JsonProcessingException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                });
        apiReqResLogger.storeLogs(merchantid, orderid, sessionReq, "SAFEX", "APICHECK",
                Utility.convertDTO2JsonString(response.getBody()), null, "COMPLETED");
        return response.getBody();
    }

    public ServiceRes sendAccountTransfer(PgDetails pgDetails, String merchantid, String sessionReq, String orderid)
            throws JsonProcessingException {
        apiReqResLogger.storeLogs(merchantid, orderid, sessionReq, "SAFEX", "FUNDTRANSFER", null, null, "INITIATED");
        ServiceReqRes serviceReqRes = new ServiceReqRes();
        serviceReqRes.setUId(pgDetails.getPgConfigKey());
        serviceReqRes.setMId(pgDetails.getPgConfigKey());
        serviceReqRes.setAgId(pgDetails.getPgConfigKey());
        serviceReqRes.setPayload(sessionReq);
        HttpResponse<ServiceRes> response = Unirest.post(requestUrl)
                .header("Connection", "keep-alive")
                .header("Content-Type", "application/json")
                .body(serviceReqRes).asObject(ServiceRes.class)
                .ifFailure(Object.class, r -> {
                    Object e = r.getBody();
                    try {
                        logger.info("Contact Response error::" + Utility.convertDTO2JsonString(e));
                        apiReqResLogger.storeLogs(merchantid, orderid, sessionReq, "SAFEX", "FUNDTRANSFER",
                                Utility.convertDTO2JsonString(e), "ERROR", "ERROR");
                    } catch (JsonProcessingException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                });
        apiReqResLogger.storeLogs(merchantid, orderid, sessionReq, "SAFEX", "FUNDTRANSFER",
                Utility.convertDTO2JsonString(response.getBody()), null, "COMPLETED");
        return response.getBody();
    }

    public ServiceRes getTransferStatus(PgDetails pgDetails, String merchantid, String sessionReq, String orderid)
            throws JsonProcessingException {
        ServiceReqRes serviceReqRes = new ServiceReqRes();
        serviceReqRes.setUId(pgDetails.getPgConfigKey());
        serviceReqRes.setMId(pgDetails.getPgConfigKey());
        serviceReqRes.setAgId(pgDetails.getPgConfigKey());
        serviceReqRes.setPayload(sessionReq);
        logger.info("FUND Status Trans req::" + Utility.convertDTO2JsonString(serviceReqRes));
        HttpResponse<ServiceRes> response = Unirest.post(requestUrl)
                .header("Connection", "keep-alive")
                .header("Content-Type", "application/json")
                .body(serviceReqRes).asObject(ServiceRes.class)
                .ifFailure(Object.class, r -> {
                    Object e = r.getBody();
                    try {
                        logger.info("Contact Response error::" + Utility.convertDTO2JsonString(e));

                    } catch (JsonProcessingException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                });
        logger.info("FUND Status Trans res::" + Utility.convertDTO2JsonString(response.getBody()));
        return response.getBody();
    }
}
