package io.asktech.payout.service.Pinwallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.asktech.payout.service.Pinwallet.dto.Authreq;
import io.asktech.payout.service.Pinwallet.dto.Authres;
import io.asktech.payout.service.Pinwallet.dto.Paymentreq;
import io.asktech.payout.service.Pinwallet.dto.Paymentres;
import io.asktech.payout.service.Pinwallet.dto.StatusReqDto;
import io.asktech.payout.utils.Utility;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

@Component
public class Authrequest {
    @Value("${Pinwallet.authUrl}")
    String authUrl;

    @Value("${Pinwallet.newPaymentUrl}")
    String newPaymentUrl;
    

    @Value("${ipddress}")
    String ipaddress;
    



    static Logger logger = LoggerFactory.getLogger(Authrequest.class);

    public Authres getToken(Authreq authreq) throws JsonProcessingException {

        HttpResponse<Authres> tResponse = Unirest
                .post(authUrl).header("Content-Type", "application/json").header("IPAddress", ipaddress)
                .body(convertDTO2JsonString(authreq)).asObject(Authres.class)
                .ifFailure(Object.class, r -> {
                    Object e = r.getBody();
                });
        // return tResponse.getBody();

        logger.info("Pinwallet Authentication : "+ Utility.convertDTO2JsonString(tResponse.getBody()));
        return tResponse.getBody();
    }

   

   


    public Paymentres paymentRequest(Paymentreq paymentreq, String token)
            throws JsonProcessingException {
        // updated
  
        logger.info("Pinwallet Paymnet request::" + convertDTO2JsonString(paymentreq));
        HttpResponse<Paymentres> tResponse = Unirest
                // .post("https://collectbot.neokred.tech/cf-svc/api/v1/external/direct-transfer" )
                .post(newPaymentUrl )
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer "+ token)
                
                // .header("client_secret", "07840e29-93f4-4ff1-a7d9-ef363c396dcc")
                // .header("program_id", "b5e0444e-d983-4876-9a38-c6d0e7b1bc21")
                .header("IPAddress", ipaddress)
                .body(paymentreq)
                .asObject(Paymentres.class)
                .ifFailure(Object.class, r -> {
                    Object e = r.getBody();
                    try {
                        logger.info(convertDTO2JsonString(e));
                    } catch (JsonProcessingException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                });
                logger.info("Pinwallet Payment Response : "+ Utility.convertDTO2JsonString(tResponse.getBody()));
        return tResponse.getBody();
    }


    public Paymentres statusRequest(StatusReqDto req, String token) throws JsonProcessingException{

        logger.info("\n status request is called\n");

        HttpResponse<Paymentres> tResponse = Unirest
                .post("https://app.pinwallet.in/api/payout/docheckstatus" )
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer "+ token)
                
                // .header("client_secret", "07840e29-93f4-4ff1-a7d9-ef363c396dcc")
                // .header("program_id", "b5e0444e-d983-4876-9a38-c6d0e7b1bc21")
                .header("IPAddress", ipaddress)
                .body(req)
                .asObject(Paymentres.class)
                .ifFailure(Object.class, r -> {
                    Object e = r.getBody();
                    try {
                        logger.info(convertDTO2JsonString(e));
                    } catch (JsonProcessingException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                });

        logger.info("\n\n\n Status Resposne  " + Utility.convertDTO2JsonString(tResponse.getBody()));
        return tResponse.getBody();

    }




    // public StatusNResDto getNStatus(StatusNReqDto statusReq, PgDetails pg)
    //         throws JsonProcessingException {

    //     HttpResponse<StatusNResDto> tResponse = Unirest
    //             // .post("https://collectbot.neokred.tech/cf-svc/api/v1/external/transaction-status")
    //             .post(newStatusUrl)   
    //             .header("Content-Type", "application/json")
    //             // .header("client_secret", "07840e29-93f4-4ff1-a7d9-ef363c396dcc")
    //             // .header("program_id", "b5e0444e-d983-4876-9a38-c6d0e7b1bc21")
    //             .header("client_secret", pg.getPgConfigKey())
    //             .header("program_id", pg.getPgConfigSecret())
    //             .body(convertDTO2JsonString(statusReq)).asObject(StatusNResDto.class)
    //             .ifFailure(Object.class, r -> {
    //                 Object e = r.getBody();
    //                 try {
    //                     logger.info(convertDTO2JsonString(e));
    //                 } catch (JsonProcessingException e1) {
    //                     // TODO Auto-generated catch block
    //                     e1.printStackTrace();
    //                 }
    //             });
    //     // System.out.println(convertDTO2JsonString(tResponse.getBody()));
    //     return tResponse.getBody();

    // }




    public String convertDTO2JsonString(Object json) throws JsonProcessingException {
        ObjectMapper Obj = new ObjectMapper();
        String jsonStr = Obj.writeValueAsString(json);
        return jsonStr;

    }

}

