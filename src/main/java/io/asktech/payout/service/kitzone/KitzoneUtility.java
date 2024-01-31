package io.asktech.payout.service.kitzone;

import java.util.Dictionary;
import java.util.Hashtable;

// import org.hibernate.annotations.common.util.impl.logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.asktech.payout.dto.merchant.AccountTransferMerReq;
import io.asktech.payout.modal.merchant.PgDetails;
import io.asktech.payout.modal.merchant.TransactionDetails;
import io.asktech.payout.service.kitzone.dto.BankDetailsEncReq;
import io.asktech.payout.service.kitzone.dto.EncPayoutReq;
import io.asktech.payout.service.kitzone.dto.EncryptedResponseDto;
import io.asktech.payout.service.kitzone.dto.EncryptionPaymentDto;
import io.asktech.payout.service.kitzone.dto.EncryptionStatusDto;
import io.asktech.payout.service.kitzone.dto.EncryptionStatusDto2;
import io.asktech.payout.service.kitzone.dto.KitzoneReqDto;
import io.asktech.payout.service.kitzone.dto.PaymentReqDto;
import io.asktech.payout.service.kitzone.dto.PayoutRes;
import io.asktech.payout.service.kitzone.dto.ResultDtIn;
import io.asktech.payout.service.kitzone.dto.ResultN;
import io.asktech.payout.service.kitzone.dto.ResultOrderId;
import io.asktech.payout.service.kitzone.dto.StatusReqByOrderId;
import io.asktech.payout.service.kitzone.dto.StatusResDto;
import io.asktech.payout.service.kitzone.dto.StatusResponseOrderId;
import io.asktech.payout.service.kitzone.model.KitzoneModel;
import io.asktech.payout.service.kitzone.repo.KitzoneRepo;
import io.asktech.payout.service.neocred.dto.StatusRes;
import io.asktech.payout.service.kitzone.dto.StatusResN;
import io.asktech.payout.utils.Utility;

@Component
public class KitzoneUtility {

    Logger logger = LoggerFactory.getLogger(KitzoneUtility.class);

    /* Start Autowired Section */
    @Autowired
    KitzoneRequest kitzoneRequest;
    @Autowired
    KitzoneRepo kitzoneRepo;

    /* End Autowired Section */

    public PayoutRes makePayment(AccountTransferMerReq dto, String merchantid, PgDetails pg,
            KitzoneModel kitzoneModel) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        Dictionary<String, String> dict = new Hashtable<>();

        dict.put("IMPS", "IFS");
        dict.put("NEFT", "RGS");
        dict.put("RTGS", "RTG");

        /* Encrypt Payment Dto */

        EncPayoutReq reqDto = new EncPayoutReq();

        BankDetailsEncReq bankDetailReq = new BankDetailsEncReq();

        // EncryptionPaymentDto reqDto = new EncryptionPaymentDto();
        reqDto.setAmount(dto.getAmount());
        reqDto.setTransferMode("banktransfer");
        reqDto.setTransferId(dto.getInternalOrderid());
        reqDto.setRemarks("");
        reqDto.setOTP("");
        reqDto.setPIN("");
        reqDto.setUserId(pg.getPgConfig3());


        // reqDto.setAccountNumber(dto.getBankaccount());
        // reqDto.setIFSCCode(dto.getIfsc());
        // Hard coded wirte now

        // reqDto.setMode(dict.get(dto.getRequestType().toUpperCase()));

        // if (dto.getIfsc().equalsIgnoreCase("ICIC0000104") || dto.getIfsc().equalsIgnoreCase("ICIC0000106") || dto.getIfsc().equalsIgnoreCase("ICIC0000103")) {

        //     reqDto.setMode("VPA");
        // } else if (dto.getIfsc().contains("ICIC")) {
            // reqDto.setIFSCCode("ICIC0000011");
            // reqDto.setMode("TPA");
        // }


        // reqDto.setOrderID(dto.getInternalOrderid());

        bankDetailReq.setBankAccount(dto.getBankaccount());
        bankDetailReq.setIfsc(dto.getIfsc());
        bankDetailReq.setName(dto.getBeneficiaryName());
        bankDetailReq.setEmail("test@gmail.com");
        bankDetailReq.setPhone(dto.getPhonenumber());
        bankDetailReq.setVpa("");
        bankDetailReq.setAddress1("test address");
        
        reqDto.setBeneDetails(bankDetailReq);


        kitzoneModel.setUserId(pg.getPgConfig3());
        kitzoneModel.setAmount(dto.getAmount());
        kitzoneModel.setMerchantId(merchantid);
        kitzoneModel.setInternalOrderId(dto.getInternalOrderid());
        kitzoneModel.setOrderId(dto.getOrderid());
        kitzoneModel.setEncryptPaymentReq(Utility.convertDTO2JsonString(reqDto));
        kitzoneModel.setTrxType(dto.getRequestType());

        logger.info("\n" + Utility.convertDTO2JsonString(reqDto) + "\n");

        kitzoneRepo.save(kitzoneModel);
        try {
            // String response = kitzoneRequest.encryptPaymentRequest(Utility.convertDTO2JsonString(reqDto), pg);
            
            String response = kitzoneRequest.encryptPayoutRequest(Utility.convertDTO2JsonString(reqDto), pg);

            
            logger.info("\n" + response + "\n");
            kitzoneModel.setEncryptPaymentRes(response);
            kitzoneRepo.save(kitzoneModel);
            EncryptedResponseDto respose_with_resultDt = objectMapper.readValue(response, EncryptedResponseDto.class);

            // FILL THE PAYMENT DTO

            KitzoneReqDto payDto = new KitzoneReqDto();
            payDto.setMsg(respose_with_resultDt.getResultDt());

            kitzoneModel.setPaymentReq(Utility.convertDTO2JsonString(payDto));
            kitzoneRepo.save(kitzoneModel);
            logger.info("\n" + Utility.convertDTO2JsonString(payDto) + "\n");

            // response = kitzoneRequest.paymentRequest(Utility.convertDTO2JsonString(payDto), pg);
            response = kitzoneRequest.paymoutRequest(Utility.convertDTO2JsonString(payDto), pg);



            logger.info("\n" + response + "\n");
            kitzoneModel.setPaymentRes(response);
            kitzoneRepo.save(kitzoneModel);
            PayoutRes payResponse = objectMapper.readValue(response, PayoutRes.class);


            logger.info("\n"+ Utility.convertDTO2JsonString(payResponse)  +"\n");
            logger.info("\n"+ Utility.convertDTO2JsonString(payResponse.getResultDt())  +"\n");
            logger.info("\n"+ Utility.convertDTO2JsonString(payResponse.getTransactionId())  +"\n");
            logger.info("\n"+ Utility.convertDTO2JsonString(payResponse.getResultDt())  +"\n");
            logger.info("\n"+ Utility.convertDTO2JsonString(payResponse.getResultDt().getData())  +"\n");


            kitzoneModel.setPgTransId(payResponse.getTransactionId());
            kitzoneModel.setStatusMessage(payResponse.getMessage());
            kitzoneRepo.save(kitzoneModel);
            return payResponse;

        } catch (Exception e) {

            logger.info(e.getMessage());
            kitzoneModel.setErrorString(e.getMessage());
            kitzoneRepo.save(kitzoneModel);
            return null;
        }
    }

    public ResultN checkStatus(KitzoneModel kitzoneModel, PgDetails pg) throws JsonProcessingException {
            // public ResultDtIn checkStatus(KitzoneModel kitzoneModel, PgDetails pg) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        logger.info("\n kitzone check status is called\n");
        EncryptionStatusDto2 req = new EncryptionStatusDto2();
        if (kitzoneModel.getPgTransId() == null) {
            kitzoneModel.setErrorString("Transaction Id Null");
            kitzoneRepo.save(kitzoneModel);
            return null;
        }
        req.setTransactionId(kitzoneModel.getPgTransId());

        /* new api */
        req.setUserId(pg.getPgConfig3());
        /* end */
        try {
            kitzoneModel.setEncryptStatusReq(Utility.convertDTO2JsonString(req));
            logger.info("\nREQ: " + Utility.convertDTO2JsonString(req) + "\n");
            String response = kitzoneRequest.encryptStatusRequest(Utility.convertDTO2JsonString(req), pg);
            logger.info("\n" + response + "\n");
            kitzoneModel.setEncryptStatusRes(response);
            kitzoneRepo.save(kitzoneModel);
            EncryptedResponseDto respose_with_resultDt = objectMapper.readValue(response, EncryptedResponseDto.class);

            KitzoneReqDto payDto = new KitzoneReqDto();
            payDto.setMsg(respose_with_resultDt.getResultDt());
            kitzoneModel.setStatusReq(Utility.convertDTO2JsonString(payDto));
            kitzoneRepo.save(kitzoneModel);
            logger.info("\n" + Utility.convertDTO2JsonString(payDto) + "\n");

            // response = kitzoneRequest.statusRequest(Utility.convertDTO2JsonString(payDto), pg);

            response = kitzoneRequest.statusRequestNew(Utility.convertDTO2JsonString(payDto), pg);
            kitzoneModel.setStatusRes(response);
            kitzoneRepo.save(kitzoneModel);

            // String responseN = response.replace("\\r\\n", "");
            // responseN = responseN.replace("\\", "");
            // EncryptedResponseDto statusRes = objectMapper.readValue(response,
            //         EncryptedResponseDto.class);
            

            StatusResN responseDto = objectMapper.readValue(response, StatusResN.class);


            logger.info("\n"+ Utility.convertDTO2JsonString(responseDto)+"\n");
            // StatusResDto statusRes = objectMapper.readValue(responseN.substring(1, responseN.length() - 1),
            //         StatusResDto.class);

            logger.info("new result : " + responseDto.getResultDt());

            // String responseN = statusRes.getResultDt().replace("\\r\\n", "");
            // responseN = responseN.replace("\\", "");
            //  logger.info("\n\nSorted Response:\t"+ responseN);
            // ResultDtIn statusRes1 = objectMapper.readValue(responseN.substring(1, responseN.length() - 1),
            // ResultDtIn.class);
            // logger.info("\n\nSorted Status Response:\t"+ Utility.convertDTO2JsonString(statusRes1));


            // logger.info("\n"+ Utility.convertDTO2JsonString(statusRes1)+"\n");


            // return statusRes1;
            return responseDto.getResultDt();

        } catch (Exception e) {
            logger.info(e.getMessage());
            kitzoneModel.setErrorString(e.getMessage());
            kitzoneRepo.save(kitzoneModel);
            return null;
        }

    }

    public ResultOrderId checkStatusByOrderId(KitzoneModel kitzoneModel, PgDetails pg,
            TransactionDetails transactionDetails) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();

        try {

            StatusReqByOrderId statusReqByOrderId = new StatusReqByOrderId();
            statusReqByOrderId.setOrderNo(transactionDetails.getInternalOrderId());

            kitzoneModel.setEncryptStatusReq(Utility.convertDTO2JsonString(statusReqByOrderId));
            kitzoneRepo.save(kitzoneModel);
            String response = kitzoneRequest.encryptStatusRequest(Utility.convertDTO2JsonString(statusReqByOrderId),
                    pg);
            kitzoneModel.setEncryptStatusRes(response);
            kitzoneRepo.save(kitzoneModel);

            EncryptedResponseDto encryptedResponse = objectMapper.readValue(response, EncryptedResponseDto.class);

            if (encryptedResponse.getResultDt() == null) {
                kitzoneModel.setStatusMessage("ON ENCYPTION REQUEST FOUND NULL RESULTdt");
                kitzoneRepo.save(kitzoneModel);
                return null;
            }

            KitzoneReqDto kitzoneReqDto = new KitzoneReqDto();
            kitzoneReqDto.setMsg(encryptedResponse.getResultDt());

            kitzoneModel.setStatusReq(Utility.convertDTO2JsonString(kitzoneReqDto));
            kitzoneRepo.save(kitzoneModel);

            response = kitzoneRequest.statustRequestByOrderId(Utility.convertDTO2JsonString(kitzoneReqDto), pg);
            kitzoneModel.setStatusRes(response);
            kitzoneRepo.save(kitzoneModel);
            StatusResponseOrderId statusResponseOrderId = objectMapper.readValue(response, StatusResponseOrderId.class);

            if (statusResponseOrderId.getResultDt() == null) {
                kitzoneModel.setStatusMessage(
                        "Resultdt in statusresponse is null it can be due to the fact that internal orderid not present their");
                kitzoneRepo.save(kitzoneModel);
            }

            return statusResponseOrderId.getResultDt();
        }

        catch (Exception e) {
            logger.info(e.getMessage());
            kitzoneModel.setStatusMessage(e.getMessage());
            kitzoneRepo.save(kitzoneModel);
            return null;
        }
    }

}
