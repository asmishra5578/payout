package io.asktech.payout.service.neocred.Request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import antlr.Token;
import io.asktech.payout.modal.merchant.PgDetails;
import io.asktech.payout.service.neocred.dto.AuthRequestDto;
import io.asktech.payout.service.neocred.dto.AuthResponseDto;
import io.asktech.payout.service.neocred.dto.PaymentDto;
import io.asktech.payout.service.neocred.dto.PaymentResDto;
import io.asktech.payout.service.neocred.dto.SettlementReqDto;
import io.asktech.payout.service.neocred.dto.SettlementResDto;
import io.asktech.payout.service.neocred.dto.StatusNReqDto;
import io.asktech.payout.service.neocred.dto.StatusNResDto;
import io.asktech.payout.service.neocred.dto.StatusReq;
import io.asktech.payout.service.neocred.dto.StatusRes;
import io.asktech.payout.utils.Utility;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

@Component
public class AuthRequest {
    @Value("${neoCred.authUrl}")
    String authUrl;
    @Value("${neoCred.settlementUrl}")
    String settlementUrl;
    @Value("${neoCred.paymentdetailsUrl}")
    String paymentdetailsUrl;

    @Value("${neoCred.newPaymentUrl}")
    String newPaymentUrl;
    @Value("${neoCred.newStatusUrl}")
    String newStatusUrl;
    



    static Logger logger = LoggerFactory.getLogger(AuthRequest.class);

    public AuthResponseDto getToken(AuthRequestDto authReqdto) throws JsonProcessingException {
        
        HttpResponse<AuthResponseDto> tResponse = Unirest
                .post(authUrl).header("Content-Type", "application/json")
                .body(convertDTO2JsonString(authReqdto)).asObject(AuthResponseDto.class)
                .ifFailure(Object.class, r -> {
                    Object e = r.getBody();
                });
        // return tResponse.getBody();
        logger.info("Neocred Auth" +Utility.convertDTO2JsonString(tResponse.getBody()));
        return tResponse.getBody();
    }
      

    public SettlementResDto paySettlement(SettlementReqDto settleReq, String clientHashId, String amount, String token)
            throws JsonProcessingException {
        // updated
        if(amount.contains(".")){
            amount = amount.split("\\.")[0];
            System.out.println(amount);
        }        
        logger.info("clientHashId::" + clientHashId);
        logger.info("amount::" + amount);
        logger.info("token::" + token);
        logger.info("settlementUrl::" + settlementUrl);
        logger.info("request::" + convertDTO2JsonString(settleReq));
        HttpResponse<SettlementResDto> tResponse = Unirest
                .post(settlementUrl)
                .header("Content-Type", "application/json")
                .header("client-Hash-Id", clientHashId)
                .header("amount", amount)
                .header("Authorization","Bearer "+token)
                .body(settleReq)
                .asObject(SettlementResDto.class)
                .ifFailure(Object.class, r -> {
                    Object e = r.getBody();
                    // try {
                    //     logger.info(convertDTO2JsonString(e));
                    // } catch (JsonProcessingException e1) {
                    //     // TODO Auto-generated catch block
                    //     e1.printStackTrace();
                    // }
                });
         
         logger.info("Neocred Settlement:"+ tResponse.getBody());
        
        return tResponse.getBody();

    }

    public SettlementResDto getStatus(StatusReq statusReq, String clientHashId, String token)
            throws JsonProcessingException {
        logger.info("clientHashId::" + clientHashId);
        logger.info("token::" + token);
        logger.info("settlementUrl::" + paymentdetailsUrl);
        logger.info("request::" + convertDTO2JsonString(statusReq));
        HttpResponse<SettlementResDto> tResponse = Unirest
                .post(paymentdetailsUrl).header("Content-Type", "application/json")
                .header("client-Hash-Id", clientHashId).header("Authorization", token)
                .body(convertDTO2JsonString(statusReq)).asObject(SettlementResDto.class)
                .ifFailure(Object.class, r -> {
                    Object e = r.getBody();
                    try {
                        logger.info(convertDTO2JsonString(e));
                    } catch (JsonProcessingException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                });
          logger.info("\n\n\nNeocred Status Called"+convertDTO2JsonString(tResponse.getBody())+"\n\n\n");
        return tResponse.getBody();

    }



//     public PaymentResDto paymentRequest(PaymentDto settleReq, PgDetails pg, String amount, String token)
    
//         throws JsonProcessingException {
    
//     // updated
  

    
//     logger.info("request::" + convertDTO2JsonString(settleReq));
    
//     HttpResponse<PaymentResDto> tResponse = Unirest
    
//             // .post("https://collectbot.neokred.tech/cf-svc/api/v1/external/direct-transfer" )
    
//             .post("https://van.neokred.tech/va/v1/ecollect/settlement" )
    
//             .header("Content-Type", "application/json")
    
//             // .header("client_secret", "07840e29-93f4-4ff1-a7d9-ef363c396dcc")
    
//             // .header("program_id", "b5e0444e-d983-4876-9a38-c6d0e7b1bc21")
//             .header("token", token)
//             .header("client-Hash-Id", pg.getPgConfig1())
    
//             .header("amount", amount)
    
//             .body(settleReq)
    
//             .asObject(PaymentResDto.class)
    
//             .ifFailure(Object.class, r -> {
    
//                 Object e = r.getBody();
    
//                 try {
    
//                     logger.info(convertDTO2JsonString(e));
    
//                 } catch (JsonProcessingException e1) {
    
//                     // TODO Auto-generated catch block
    
//                     e1.printStackTrace();
    
//                 }
    
//             });


    
//     return tResponse.getBody();


    
// }



    public PaymentResDto paymentRequest(PaymentDto settleReq, PgDetails pg)
            throws JsonProcessingException {
       
  
        logger.info("request::" + convertDTO2JsonString(settleReq));
        HttpResponse<PaymentResDto> tResponse = Unirest
                //.post("https://collectbot.neokred.tech/cf-svc/api/v1/external/direct-transfer" )
                .post(newPaymentUrl )
                .header("Content-Type", "application/json")
                // .header("client_secret", "07840e29-93f4-4ff1-a7d9-ef363c396dcc")
                // .header("program_id", "b5e0444e-d983-4876-9a38-c6d0e7b1bc21")
                .header("client_secret", pg.getPgConfigKey())
                .header("program_id", pg.getPgConfigSecret())
                .body(settleReq)
                .asObject(PaymentResDto.class)
                .ifFailure(Object.class, r -> {
                    Object e = r.getBody();
                    try {
                        logger.info(convertDTO2JsonString(e));
                    } catch (JsonProcessingException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                });

        return tResponse.getBody();

    }

    public StatusRes getNStatus(StatusNReqDto statusReq, PgDetails pg)
    throws JsonProcessingException {

HttpResponse<StatusRes> tResponse = Unirest
        // .post("https://collectbot.neokred.tech/cf-svc/api/v1/external/transaction-status")
        .post(newStatusUrl)   
        .header("Content-Type", "application/json")
        // .header("client_secret", "07840e29-93f4-4ff1-a7d9-ef363c396dcc")
        // .header("program_id", "b5e0444e-d983-4876-9a38-c6d0e7b1bc21")
        .header("client-Hash-Id", pg.getPgConfig1())
        .body(convertDTO2JsonString(statusReq)).asObject(StatusRes.class)
        .ifFailure(Object.class, r -> {
            Object e = r.getBody();
            try {
                logger.info(convertDTO2JsonString(e));
            } catch (JsonProcessingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
logger.info("Neocred Status: "+convertDTO2JsonString(tResponse.getBody()));
return tResponse.getBody();

}



    // public StatusNResDto getNStatus(StatusNReqDto statusReq, PgDetails pg)
    //         throws JsonProcessingException {

    //     HttpResponse<StatusNResDto> tResponse = Unirest
    //             // .post("https://collectbot.neokred.tech/cf-svc/api/v1/external/transaction-status")
    //             .post(newStatusUrl)   
    //             .header("Content-Type", "application/json")
    // //             // .header("client_secret", "07840e29-93f4-4ff1-a7d9-ef363c396dcc")
    // //             // .header("program_id", "b5e0444e-d983-4876-9a38-c6d0e7b1bc21")
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
    // //     // System.out.println(convertDTO2JsonString(tResponse.getBody()));
    //     return tResponse.getBody();

    // }




    public String convertDTO2JsonString(Object json) throws JsonProcessingException {
        ObjectMapper Obj = new ObjectMapper();
        String jsonStr = Obj.writeValueAsString(json);
        return jsonStr;

    }

}
