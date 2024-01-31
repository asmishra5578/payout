package io.asktech.payout.service.ICICIPayout;
import java.security.InvalidAlgorithmParameterException;
import java.security.spec.InvalidKeySpecException;
// import org.hibernate.annotations.common.util.impl.logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.asktech.payout.dto.merchant.AccountTransferMerReq;
import io.asktech.payout.dto.reqres.TransferStatusReq;
import io.asktech.payout.modal.merchant.PgDetails;
import io.asktech.payout.service.ICICIPayout.Dto.FundTransaferreq;
import io.asktech.payout.service.ICICIPayout.Dto.FundTransferres;
import io.asktech.payout.service.ICICIPayout.Dto.TokenRequest;
import io.asktech.payout.service.ICICIPayout.Dto.TokenResponse;
import io.asktech.payout.service.ICICIPayout.Dto.TransactionStatusreq;
import io.asktech.payout.service.ICICIPayout.Dto.TransactionStatusres;
import io.asktech.payout.service.ICICIPayout.model.IcicipayoutTransactiondetails;
import io.asktech.payout.service.ICICIPayout.repo.Icicirepository;
import io.asktech.payout.utils.Utility;

@Component
public class IciciUtility {

    @Autowired
    IciciRequest iciciRequest;
    @Autowired
    AesEncryption aesEncryption;
   @Autowired
   Icicirepository icicirepository;
    public static Logger logger = LoggerFactory.getLogger(IciciUtility.class);
     ObjectMapper objectMapper = new ObjectMapper();

   
    public FundTransferres makePayload(AccountTransferMerReq dto, String merchantid, PgDetails pg){
   TokenRequest tokenRequest = new TokenRequest();
   FundTransaferreq fundTransaferreq = new FundTransaferreq();
   IcicipayoutTransactiondetails icicipayoutTransactiondetails = new IcicipayoutTransactiondetails();
   tokenRequest.setUserId(pg.getPgConfigKey());
   tokenRequest.setPassword(pg.getPgConfigSecret());

   fundTransaferreq.setWalletId(pg.getPgConfig1()); 
   fundTransaferreq.setAmount(dto.getAmount());
   fundTransaferreq.setBeneficiaryAccount(dto.getBankaccount());
   fundTransaferreq.setBeneficiaryIFSC(dto.getIfsc());
   fundTransaferreq.setBeneficiaryAddress("");
   fundTransaferreq.setBeneficiaryName(dto.getBeneficiaryName());
   fundTransaferreq.setDebitAccount(pg.getPgConfig3());
   fundTransaferreq.setCIbPayment(false);
   fundTransaferreq.setPaymentMode(dto.getRequestType().toUpperCase());
   fundTransaferreq.setRemarks(dto.getPurpose());

   
   String x_zyro_key = pg.getPgConfig1();
   try{

   icicipayoutTransactiondetails.setAmount(fundTransaferreq.getAmount());
   icicipayoutTransactiondetails.setDebitaccount(fundTransaferreq.getDebitAccount());
   icicipayoutTransactiondetails.setMerchantId(merchantid);
   icicipayoutTransactiondetails.setOrderId(dto.getInternalOrderid());
     
   String token = iciciRequest.getToken(tokenRequest.getUserId(), tokenRequest.getPassword());
   logger.info("\n\nTOKEN RESPONSE:\t"+token);
  
    

   TokenResponse tokenResponse = objectMapper.readValue(token, TokenResponse.class);
   logger.info("TOKEN RESPONSE DTO:\t"+Utility.convertDTO2JsonString(tokenResponse));
   logger.info("\n\nFUNDTRANSFER REQ"+Utility.convertDTO2JsonString(fundTransaferreq));
   
   icicipayoutTransactiondetails.setTokenreq(Utility.convertDTO2JsonString(tokenRequest));
   icicipayoutTransactiondetails.setToken(tokenResponse.getData().getToken());

   String payload = aesEncryption.encrypt(Utility.convertDTO2JsonString(fundTransaferreq), pg.getPgConfig2(), pg.getPgConfig2().substring(0, 15));
   logger.info("\n\nICICI FUND TRANSFER ENCRYPTED PAYLOAD:\t"+payload);

   icicipayoutTransactiondetails.setPaymentReq(Utility.convertDTO2JsonString(fundTransaferreq));
   icicipayoutTransactiondetails.setEncryptPaymentReq(payload);
    String fundresponse = iciciRequest.fundTransferrequest(payload, x_zyro_key, tokenResponse.getData().getToken());
    logger.info("\n\nICICI PAYLOAD RESPONSE:\t"+fundresponse);

    FundTransferres fundTransferres;
    
        fundTransferres = objectMapper.readValue(fundresponse, FundTransferres.class);
        icicipayoutTransactiondetails.setPaymentRes(Utility.convertDTO2JsonString(fundTransferres));
        icicipayoutTransactiondetails.setPgTransId(fundTransferres.getData().getTransactionId());
        icicipayoutTransactiondetails.setPgOrderId(fundTransferres.getData().getOrderId());
        icicirepository.save(icicipayoutTransactiondetails);

    return fundTransferres;
}catch (Exception e){
    e.printStackTrace();
    return null;
}
}

public TransactionStatusres gettransactionstatus(String dto, String merchantid, PgDetails pg, String orderId) throws InvalidKeySpecException, InvalidAlgorithmParameterException{

   TokenRequest tokenRequest = new TokenRequest();
   IcicipayoutTransactiondetails icicipayoutTransactiondetails = icicirepository.findByOrderId(dto);
    TransactionStatusreq transactionStatusreq = new TransactionStatusreq();
     tokenRequest.setUserId(pg.getPgConfigKey());
   tokenRequest.setPassword(pg.getPgConfigSecret());

    transactionStatusreq.setOrderId(icicipayoutTransactiondetails.getPgOrderId());
    transactionStatusreq.setWalletId(pg.getPgConfig1());

    String x_zyro_key = pg.getPgConfig1();
   
     String token = iciciRequest.getToken(tokenRequest.getUserId(), tokenRequest.getPassword());
   logger.info("\n\nTOKEN RESPONSE:\t"+token);
  
    

   TokenResponse tokenResponse;
try {
    tokenResponse = objectMapper.readValue(token, TokenResponse.class);
 
   logger.info("TOKEN RESPONSE DTO:\t"+Utility.convertDTO2JsonString(tokenResponse));
  
   String payload = aesEncryption.encrypt(Utility.convertDTO2JsonString(transactionStatusreq), pg.getPgConfig2(), pg.getPgConfig2().substring(0, 15));
   logger.info("\n\nICICI STATUS API ENCRYPTED PAYLOAD:\t"+payload);

   icicipayoutTransactiondetails.setStatusReq(Utility.convertDTO2JsonString(transactionStatusreq));
    String transactionStatusresponse = iciciRequest.getTransactionStatus(payload, x_zyro_key, tokenResponse.getData().getToken());
    logger.info("\n\nICICI STATUS API PAYLOAD RESPONSE:\t"+transactionStatusresponse);

    TransactionStatusres transactionStatusres;
    
       transactionStatusres = objectMapper.readValue(transactionStatusresponse, TransactionStatusres.class);
       logger.info("\n\nICICI PAYLOAD RESPONSE:\t"+Utility.convertDTO2JsonString(transactionStatusres));
       icicipayoutTransactiondetails.setStatusRes(Utility.convertDTO2JsonString(transactionStatusres));
       icicipayoutTransactiondetails.setStatus(transactionStatusres.getData().getStatus());
       icicirepository.save(icicipayoutTransactiondetails);

         return transactionStatusres;
}catch (JsonMappingException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
    return null;
} catch (JsonProcessingException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
    return null;
}


   
}


}
