package io.asktech.payout.service.cashfree.requests;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.asktech.payout.dto.reqres.cashfree.AuthenticationRes;
import io.asktech.payout.service.cashfree.dto.error;
import io.asktech.payout.utils.Utility;
import kong.unirest.Unirest;

@Component
public class AutherizationRequest {
    @Value("${cashfreeCredentials.authUrl}")
    String authUrl;

    static Logger logger = LoggerFactory.getLogger(AutherizationRequest.class);

    public AuthenticationRes cashfreeAuthToken(String ClientId, String ClientSecret) throws JsonProcessingException {
        logger.info("Cashfree Auth Request|" + ClientId + "|" + ClientSecret);
        AuthenticationRes authenticationRes = Unirest.post(authUrl).header("X-Client-Id", ClientId)
                .header("X-Client-Secret", ClientSecret).asObject(AuthenticationRes.class)
                .ifFailure(error.class, r -> {
                    error e = r.getBody();
                    try {
                        logger.info(Utility.convertDTO2JsonString(e));
                    } catch (JsonProcessingException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }).getBody();
        logger.info(Utility.convertDTO2JsonString(authenticationRes));
        return authenticationRes;

    }
}
