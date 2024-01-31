package io.asktech.payout.service.Pinwallet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.asktech.payout.dto.merchant.AccountTransferMerReq;
import io.asktech.payout.modal.merchant.PgDetails;
import io.asktech.payout.service.Pinwallet.Repository.Pinwalletrepository;
import io.asktech.payout.service.Pinwallet.dto.Authreq;
import io.asktech.payout.service.Pinwallet.dto.Authres;
import io.asktech.payout.service.Pinwallet.dto.Paymentreq;
import io.asktech.payout.service.Pinwallet.dto.Paymentres;
import io.asktech.payout.service.Pinwallet.model.Pinwalledpayouttrnxs;
import io.asktech.payout.utils.Utility;

@Component
public class Pinwalletutils {
    static Logger logger = LoggerFactory.getLogger(Pinwalletutils.class);

    @Autowired
    Authrequest authrequest;
    @Autowired
    Pinwalletrepository pinwalletrepository;

    @Autowired
    Pinwalledpayouttrnxs pinwalledpayouttrnxs;

    public Paymentres doPayment(AccountTransferMerReq dto, String merchantid, PgDetails pg,
            Pinwalledpayouttrnxs pinwalledpayouttrnxs)
            throws JsonProcessingException {
        Authreq authreq = new Authreq();
        Paymentreq paymentreq = new Paymentreq();

        authreq.setUserName(pg.getPgConfigKey());
        authreq.setPassword(pg.getPgConfigSecret());

        Authres authres = authrequest.getToken(authreq);

        pinwalledpayouttrnxs.setAuthRequest(Utility.convertDTO2JsonString(authreq));
        pinwalledpayouttrnxs.setAuthResponse(Utility.convertDTO2JsonString(authres));
        pinwalledpayouttrnxs.setMerchantId(merchantid);
        pinwalledpayouttrnxs.setUserName(authreq.getUserName());
        pinwalledpayouttrnxs.setTrxType(dto.getRequestType());
        paymentreq.setAmount(dto.getAmount());
        paymentreq.setBenificiaryAccount(dto.getBankaccount());
        paymentreq.setBenificiaryIfsc(dto.getIfsc());
        paymentreq.setBenificiaryName(dto.getBeneficiaryName());
        paymentreq.setLatitude("11.1");
        paymentreq.setLongitude("77.35");
        paymentreq.setTransactionId(pinwalledpayouttrnxs.getPgOrderId());
        pinwalledpayouttrnxs.setOrderId(dto.getInternalOrderid());
        pinwalletrepository.save(pinwalledpayouttrnxs);
        Paymentres paymentres = authrequest.paymentRequest(paymentreq, authres.getData().getToken());

        if (paymentres.getResponseCode() == 400) {
            logger.info("ERROR in Response");
        }
    
        pinwalledpayouttrnxs.setAmount(paymentreq.getAmount());
        pinwalledpayouttrnxs.setStatus(paymentres.getSuccess());
        pinwalledpayouttrnxs.setStatusMessage(paymentres.getMessage());
        pinwalledpayouttrnxs.setPassword(authreq.getPassword());
        pinwalledpayouttrnxs.setPaymentRequest(Utility.convertDTO2JsonString(paymentreq));
        pinwalledpayouttrnxs.setPaymentResponse(Utility.convertDTO2JsonString(paymentres));
        pinwalletrepository.save(pinwalledpayouttrnxs);

        return paymentres;
    }

}
