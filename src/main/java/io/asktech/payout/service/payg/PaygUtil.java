package io.asktech.payout.service.payg;

import org.springframework.stereotype.Component;

import io.asktech.payout.modal.merchant.PgDetails;
import io.asktech.payout.utils.Utility;

@Component
public class PaygUtil {
    public String getSignature(PgDetails pgDetails) {
        String paygAuth = pgDetails.getPgConfigKey() + ":" + pgDetails.getPgConfigSecret() + ":M:"
                + pgDetails.getPgConfig1();
        return Utility.base64Encoder(paygAuth);
    }
}
