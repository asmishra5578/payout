package io.asktech.payout.service.razor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.asktech.payout.modal.merchant.PgDetails;
import io.asktech.payout.service.razor.dto.RazorResponseDto;
import io.asktech.payout.service.razor.dto.req.RazorBankAccountRequestDto;
import io.asktech.payout.service.razor.dto.req.RazorStatusResponse;
import io.asktech.payout.utils.ApiReqResLogger;
import io.asktech.payout.utils.Utility;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

@Component
public class RazorRequests {
    static Logger logger = LoggerFactory.getLogger(RazorRequests.class);

    @Autowired
    ApiReqResLogger apiReqResLogger;

    @Value("${razor.acctransfer}")
    String requestUrl;

    public RazorResponseDto accountTransfer(RazorBankAccountRequestDto razorRequestDto,
            String merchantid, PgDetails pg) throws JsonProcessingException {
        String orderid = razorRequestDto.getReference_id();
        String username = pg.getPgConfigKey();
        String password = pg.getPgConfigSecret();
        String req = Utility.convertDTO2JsonString(razorRequestDto);
        apiReqResLogger.storeLogs(merchantid, orderid, req, "RAZOR",
                "ACCTRANSFER", null, null, "INITIATED");
        logger.info("RAZOR REQ::" + requestUrl + "-" + req);

        HttpResponse<String> response = Unirest.post(requestUrl).basicAuth(username, password)
                .header("Connection", "keep-alive")
                .header("Content-Type", "application/json")
                .body(req).asString();

        logger.info("RAZOR RES::" + response.getBody());
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, false);

        RazorResponseDto razorResDto = objectMapper.readValue(response.getBody(), RazorResponseDto.class);
        apiReqResLogger.storeLogs(merchantid, orderid, null, "RAZOR",
                "ACCTRANSFER", response.getBody(), null, "COMPLETE");
        logger.info("RAZOR RES MAPPED::" + Utility.convertDTO2JsonString(razorResDto));
        return razorResDto;
    }

    public RazorResponseDto upiTransfer(RazorBankAccountRequestDto razorRequestDto,
            String merchantid, PgDetails pg) throws JsonProcessingException {
        String orderid = razorRequestDto.getReference_id();
        String username = pg.getPgConfigKey();
        String password = pg.getPgConfigSecret();
        String req = Utility.convertDTO2JsonString(razorRequestDto);
        apiReqResLogger.storeLogs(merchantid, orderid, req, "RAZOR",
                "UPITRANSFER", null, null, "INITIATED");
        logger.info("RAZOR REQ::" + requestUrl + "-" + req);

        HttpResponse<String> response = Unirest.post(requestUrl).basicAuth(username, password)
                .header("Connection", "keep-alive")
                .header("Content-Type", "application/json")
                .body(req).asString();

        logger.info("RAZOR RES::" + response.getBody());
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, false);

        RazorResponseDto razorResDto = objectMapper.readValue(response.getBody(), RazorResponseDto.class);
        apiReqResLogger.storeLogs(merchantid, orderid, null, "RAZOR",
                "UPITRANSFER", response.getBody(), null, "COMPLETE");
        logger.info("RAZOR RES MAPPED::" + Utility.convertDTO2JsonString(razorResDto));
        return razorResDto;
    }

    @Value("${razor.statusReq}")
    String statusLink;

    public RazorStatusResponse transferStatusApi(String orderId,
            String merchantid, PgDetails pg) throws JsonProcessingException {
        String username = pg.getPgConfigKey();
        String password = pg.getPgConfigSecret();

        String statusReq = statusLink + "/" + orderId;
        logger.info("RAZOR REQ BODY::" + statusReq);
        HttpResponse<RazorStatusResponse> response = Unirest.get(statusReq).basicAuth(username, password)
                .header("Connection", "keep-alive")
                .header("Content-Type", "application/json").asObject(RazorStatusResponse.class)
                .ifFailure(Object.class, r -> {
                    Object e = r.getBody();
                    try {
                        logger.info("RAZOR  Response error::" + Utility.convertDTO2JsonString(e));
                    } catch (JsonProcessingException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                });
        logger.info("RAZOR RES BODY::" + Utility.convertDTO2JsonString(response.getBody()));
        return response.getBody();
    }
}
