package io.asktech.payout.service.ICICIPayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import antlr.Token;
import io.asktech.payout.modal.merchant.PgDetails;
import io.asktech.payout.service.ICICIPayout.Dto.Payload;
import io.asktech.payout.service.ICICIPayout.Dto.TokenRequest;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

@Component
public class IciciRequest {
   
   public static Logger logger = LoggerFactory.getLogger(IciciRequest.class);
    
  public String getToken(String username, String password){
HttpResponse<String> response = Unirest.post("https://payout.myzyro.com/api/ZyroBanking/UserLogin")
  .header("Content-Type", "application/json")
  .body("{\n\"userId\":\""+username+"\",\n\"password\":\""+password+"\"}")
  .asString();
     
  return response.getBody();
}

 
public String fundTransferrequest(String payload, String x_zyro_key, String token){
    
HttpResponse<String> response = Unirest.post("https://payout.myzyro.com/api/ZyroBanking/FundTransfer")
  .header("X-Zyro-Key", x_zyro_key)
  .header("authorization", "Bearer "+ token)
  .header("Content-Type", "application/json")
  .body("{\"payload\":\""+payload+"\"}")
  .asString();
  logger.info("\n\n"+response.getBody()+"\n\n");
  return response.getBody();
  }

 public String getTransactionStatus(String payload, String x_zyro_key, String token){
HttpResponse<String> response = Unirest.post("https://payout.myzyro.com/api/ZyroBanking/CheckTransactionDetails")
  .header("X-Zyro-Key", x_zyro_key)
  .header("Authorization", "Bearer "+ token)
  .header("Content-Type", "application/json")
  .body("{\"payload\":\""+payload+"\"}")
  .asString();
    
  return response.getBody();
}
}
