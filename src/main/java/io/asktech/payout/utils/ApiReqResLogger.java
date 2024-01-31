package io.asktech.payout.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import io.asktech.payout.modal.merchant.ApiRequestResponseLogger;
import io.asktech.payout.repository.reqres.ApiRequestResponseLoggerRepo;

@Component
public class ApiReqResLogger {
    @Autowired
    ApiRequestResponseLoggerRepo apiRequestResponseLoggerRepo;

    @Async
    public void storeLogs(String merchantid, String orderid, String request, String serviceProvder, String apiType,
            String response, String errorStatus, String status) {
        List<ApiRequestResponseLogger> reqs = apiRequestResponseLoggerRepo.getByRequestIdAndMerchantId(orderid, merchantid);
        ApiRequestResponseLogger apiRequestResponseLogger = null;
        if (reqs.size() > 0) {
            apiRequestResponseLogger = reqs.get(0);
        } else {
            apiRequestResponseLogger = new ApiRequestResponseLogger();
        }
        apiRequestResponseLogger.setMerchantId(merchantid);
        apiRequestResponseLogger.setRequestId(orderid);
        apiRequestResponseLogger.setRequest(request);
        apiRequestResponseLogger.setServiceProvider(serviceProvder);
        apiRequestResponseLogger.setApiType(apiType);
        apiRequestResponseLogger.setResponse(response);
        apiRequestResponseLogger.setErrorStatus(errorStatus);
        apiRequestResponseLogger.setStatus(status);
        apiRequestResponseLoggerRepo.save(apiRequestResponseLogger);
    }
}
