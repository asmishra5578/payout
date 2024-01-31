package io.asktech.payout.service.cashfree;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.asktech.payout.dto.reqres.cashfree.AuthenticationRes;
import io.asktech.payout.enums.FormValidationExceptionEnums;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.service.cashfree.dto.AuthDetails;
import io.asktech.payout.service.cashfree.dto.CustomerAccDetails;
import io.asktech.payout.service.cashfree.dto.accountVerificationDto;
import io.asktech.payout.service.cashfree.dto.error;
import io.asktech.payout.service.cashfree.requests.AutherizationRequest;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

@Component
public class AccountVerificationUtils {

    private static final String SYSTEM_EXCEPTION_ES1001 = "Request Failed";

    static Logger logger = LoggerFactory.getLogger(AccountVerificationUtils.class);

    @Autowired
    AutherizationRequest autherizationRequest;
    private static final String CONTENTTYPE = "Content-Type";
    private static final String APPTYPE = "application/json";
    private static final String AUTHORIZATION = "Authorization";
    private static final String STATUSURL = "https://payout-api.cashfree.com/payout/v1.2/validation/bankDetails";

    public accountVerificationDto bankAccountVerification(CustomerAccDetails accDetails, AuthDetails auth)
            throws JsonProcessingException, ValidationExceptions {
        AuthenticationRes authRes = autherizationRequest.cashfreeAuthToken(auth.getClientId(), auth.getClientSecret());
        String token = null;
        if (authRes.getStatus().equalsIgnoreCase("SUCCESS")) {
            token = authRes.getData().getToken();
        }
        try {
            HttpResponse<accountVerificationDto> walletTransferRes = Unirest.post(STATUSURL)
                    .header(CONTENTTYPE, APPTYPE).header(AUTHORIZATION, "Bearer " + token)
                    .asObject(accountVerificationDto.class)
                    .ifFailure(error.class, r -> {
                        error e = r.getBody();
                    });
            return walletTransferRes.getBody();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ValidationExceptions(SYSTEM_EXCEPTION_ES1001, FormValidationExceptionEnums.ES1001);
        }

    }

    public accountVerificationDto asyncBankAccountVerification(CustomerAccDetails accDetails) {
        return null;
    }

    public accountVerificationDto UPIVerification(CustomerAccDetails accDetails) {
        return null;
    }
}
