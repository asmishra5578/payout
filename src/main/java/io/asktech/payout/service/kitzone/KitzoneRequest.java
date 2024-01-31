package io.asktech.payout.service.kitzone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.asktech.payout.modal.merchant.PgDetails;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

@Component
public class KitzoneRequest {
    Logger logger = LoggerFactory.getLogger(KitzoneRequest.class);

    /* Start @value section*/

    @Value("${Kitzone.encyptUrl}")
    String encyptUrl;
    @Value("${Kitzone.paymentUrl}")
    String paymentUrl;
    @Value("${Kitzone.statusUrl}")
    String statusUrl;
    @Value("${Kitzone.stausUrlByOrderid}")
    String stausUrlByOrderid;
    /* End @value Section */


    public String encryptPaymentRequest(String req, PgDetails pg) {

        logger.info("Encryption Request: " + req);

        HttpResponse<JsonNode> httpRes = Unirest
                // .post("https://services.kitzone.in/api/UserServices/eEncryptRequest")
                .post(encyptUrl)
                .header("Content-Type", "application/json")
                // .header("KitzoneSecretKey", "4Bo2Rv2wLlau7R8jiVJ2KKUY")
                .header("KitzoneSecretKey", pg.getPgConfigSecret())
                .body(req).asJson();

        logger.info("Encryption Request: " + httpRes.getBody().toPrettyString());

        return httpRes.getBody().toPrettyString();

    }

    public String encryptStatusRequest(String req, PgDetails pg) {

        logger.info("Encryption Request: " + req);

        HttpResponse<JsonNode> httpRes = Unirest
                // .post("https://services.kitzone.in/api/UserServices/eEncryptRequest")
                .post(encyptUrl)
                .header("Content-Type", "application/json")
                // .header("KitzoneSecretKey", "GGMO9lMvT78ekA5b1v8akZvR")
                .header("KitzoneSecretKey", pg.getPgConfig1())
                .body(req).asJson();

        logger.info("Encryption Request: " + httpRes.getBody().toPrettyString());

        return httpRes.getBody().toPrettyString();
    }



    public String paymentRequest(String req, PgDetails pg) {

        logger.info("Encryption Request: " + req);

        HttpResponse<JsonNode> httpRes = Unirest
                // .post("https://services.kitzone.in/api/UserServices/ekitzoneService")
                .post(paymentUrl)
                .header("Content-Type", "application/json")
                // .header("kitzoneToken", "4Bo2Rv2wLlau7R8jiVJ2KKUYUL4xwFIexD3")
                .header("kitzoneToken", pg.getPgConfigKey())

                .body(req).asJson();

        logger.info("Encryption Request: " + httpRes.getBody().toPrettyString());

        return httpRes.getBody().toPrettyString();
    }

    public String statusRequest(String req,PgDetails pg) {

        logger.info("Encryption Request: " + req);

        HttpResponse<String> httpRes = Unirest
                // .post("https://services.kitzone.in/api/UserServices/ekitzoneCheckTransactionStatus")
                .post(statusUrl)
                .header("Content-Type", "application/json")
                .accept("application/json")
                // .header("kitzoneToken", "GGMO9lMvT78ekA5b1v8akZvRcQPTr3r4sg8")
                .header("kitzoneToken", pg.getPgConfig2())
                .body(req).asString();

        logger.info("Encryption Request: " + httpRes.getBody());

        return httpRes.getBody();
    }



    public String statustRequestByOrderId(String req, PgDetails pg) {

        logger.info("Encryption Request: " + req);

        HttpResponse<JsonNode> httpRes = Unirest
                .post(stausUrlByOrderid)
                .header("Content-Type", "application/json")
                .header("kitzoneToken", pg.getPgConfig2())

                .body(req).asJson();

        logger.info("Encryption Request: " + httpRes.getBody().toPrettyString());

        return httpRes.getBody().toPrettyString();
    }

  



    /*  new api integration */

    public String encryptPayoutRequest(String req, PgDetails pg) {

        logger.info("Encryption Request: " + req);

        HttpResponse<JsonNode> httpRes = Unirest
                // .post("https://services.kitzone.in/api/UserServices/eEncryptRequest")
                .post(encyptUrl)
                .header("Content-Type", "application/json")
                // .header("KitzoneSecretKey", "4Bo2Rv2wLlau7R8jiVJ2KKUY")
                .header("KitzoneSecretKey", pg.getPgConfigSecret())
                .body(req).asJson();

        logger.info("Encryption Request: " + httpRes.getBody().toPrettyString());

        return httpRes.getBody().toPrettyString();

    }


    public String paymoutRequest(String req, PgDetails pg) {

        logger.info("Encryption Request: " + req);

        HttpResponse<JsonNode> httpRes = Unirest
                .post("https://services.kitzone.in/api/UserServices/ekitzoneDMT")
                // .post(paymentUrl)
                .header("Content-Type", "application/json")
                // .header("kitzoneToken", "4Bo2Rv2wLlau7R8jiVJ2KKUYUL4xwFIexD3")
                .header("kitzoneToken", pg.getPgConfigKey())

                .body(req).asJson();

        logger.info("Encryption Request: " + httpRes.getBody().toPrettyString());

        return httpRes.getBody().toPrettyString();
    }

public String statusRequestNew(String req,PgDetails pg) {

        logger.info("Encryption Request: " + req);

        HttpResponse<String> httpRes = Unirest
                .post("https://services.kitzone.in/api/UserServices/ekitzoneDMTStatus")
                // .post(statusUrl)
                .header("Content-Type", "application/json")
                .accept("application/json")
                // .header("kitzoneToken", "GGMO9lMvT78ekA5b1v8akZvRcQPTr3r4sg8")
                .header("kitzoneToken", pg.getPgConfig2())
                .body(req).asString();

        logger.info("Encryption Request: " + httpRes.getBody());

        return httpRes.getBody();
    }



}
