package io.asktech.payout.service.safex;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.asktech.payout.dto.merchant.AccountTransferMerReq;
import io.asktech.payout.enums.FormValidationExceptionEnums;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.modal.merchant.PgDetails;
import io.asktech.payout.service.safex.dto.ApiVersionCheck;
import io.asktech.payout.service.safex.dto.Header;
import io.asktech.payout.service.safex.dto.PayOutBean;
import io.asktech.payout.service.safex.dto.ResPayload;
import io.asktech.payout.service.safex.dto.SafexRequestDto;
import io.asktech.payout.service.safex.dto.SafexResponseDto;
import io.asktech.payout.service.safex.dto.ServiceReqRes;
import io.asktech.payout.service.safex.dto.ServiceRes;
import io.asktech.payout.service.safex.dto.Transaction;
import io.asktech.payout.service.safex.utils.Encryption;
import io.asktech.payout.utils.Utility;

@Component
public class SafexUtil {

    static Logger logger = LoggerFactory.getLogger(SafexUtil.class);
    public static final String SAFEX_RESP_FAILED = "Request Failed";

    @Autowired
    SaveSafexDetails saveDetails;

    // ---------------------------------------STATUS API ----------------------------------
    public String getStatusAPIRequest(String orderId, String key, PgDetails pgDetails)
            throws ValidationExceptions, JsonProcessingException {
        Header header = new Header();
        header.setOperatingSystem("WEB");
        header.setSessionId(pgDetails.getPgConfigKey());
        Transaction transaction = new Transaction();
        transaction.setRequestSubType("STCHK");
        transaction.setRequestType("TMH");
        PayOutBean payOutBean = new PayOutBean();
        String payid = saveDetails.getPayoutBean(orderId);
        logger.info("PAYID::"+payid);
        if (payid == null) {
            throw new ValidationExceptions("Order Details not found", FormValidationExceptionEnums.ES1001);
        }
        payOutBean.setPayoutId(saveDetails.getPayoutBean(orderId));
        payOutBean.setOrderRefNo(orderId);
        SafexRequestDto safexRequestDto = new SafexRequestDto();
        safexRequestDto.setHeader(header);
        safexRequestDto.setPayOutBean(payOutBean);
        safexRequestDto.setTransaction(transaction);
        String statustrxReq = Utility.convertDTO2JsonString(safexRequestDto);
        saveDetails.saveSafexRequest(orderId, null, null, null, null,
                statustrxReq, null, null, null,
                null, null, "STATUSREQ");
        logger.info("STATUSREQ API::" + orderId + "|" + statustrxReq);
        String paystring = Encryption.encrypt(Utility.convertDTO2JsonString(safexRequestDto), key);
        return paystring;
    }

    public SafexResponseDto getStatusAPIResponse(String encryptedPayload, String key, String orderId)
            throws ValidationExceptions, JsonMappingException, JsonProcessingException {
        String decryptedPayload = Encryption.decrypt(encryptedPayload, key);
        ObjectMapper ObjMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SafexResponseDto resPayload = ObjMapper.readValue(decryptedPayload, SafexResponseDto.class);
        if (resPayload == null) {
            logger.error("StatusAPIResponse RespPayload not found");
            throw new ValidationExceptions(SAFEX_RESP_FAILED, FormValidationExceptionEnums.ES1001);
        }
        if (resPayload.getHeader() == null) {
            logger.error(" StatusAPIResponse RespPayload.header not found");
            throw new ValidationExceptions(SAFEX_RESP_FAILED, FormValidationExceptionEnums.ES1001);
        }
        saveDetails.saveSafexRequest(orderId, null, null, null, null,
                null, Utility.convertDTO2JsonString(resPayload), null, null,
                resPayload.getHeader().getRequestId(), null, "STATUSRES");
        logger.info("STATUSRES API::" + orderId + "|" + Utility.convertDTO2JsonString(resPayload));
        saveDetails.savePayloadBean(resPayload.getPayOutBean());
        return resPayload;
    }

    // ----------------------------------------FUND Transfer
    // ---------------------------------
    public String getFundTransferRequest(AccountTransferMerReq dto, String key, PgDetails pg)
            throws JsonProcessingException {

        String secret = key;

        Header header = new Header();
        header.setOperatingSystem("ANDROID");
        header.setSessionId(pg.getPgConfigKey());

        Transaction transaction = new Transaction();
        transaction.setRequestSubType("PWTB");
        transaction.setRequestType("WTW");

        PayOutBean payOutBean = new PayOutBean();

        payOutBean.setAccountHolderName(dto.getBeneficiaryName());
        payOutBean.setAccountNo(dto.getBankaccount());
        payOutBean.setAccountType("Saving");
        payOutBean.setBankName("Kotak");
        payOutBean.setIfscCode(dto.getIfsc());
        payOutBean.setMobileNo(dto.getPhonenumber());
        payOutBean.setTxnAmount(dto.getAmount());
        payOutBean.setTxnType(dto.getRequestType());
        payOutBean.setOrderRefNo(dto.getInternalOrderid());
        saveDetails.savePayloadBean(payOutBean);
        SafexRequestDto safexRequestDto = new SafexRequestDto();
        safexRequestDto.setHeader(header);
        safexRequestDto.setPayOutBean(payOutBean);
        safexRequestDto.setTransaction(transaction);
        String fundstrxReq = Utility.convertDTO2JsonString(safexRequestDto);
        saveDetails.saveSafexRequest(dto.getInternalOrderid(), fundstrxReq, null, null, null,
                null, null, null, null,
                null, null, "FUNDREQ");
        logger.info("FUNDREQ API::" + dto.getInternalOrderid() + "|" + fundstrxReq);
        String paystring = Encryption.encrypt(Utility.convertDTO2JsonString(safexRequestDto), secret);
        // System.out.println(Utility.convertDTO2JsonString(serviceReqRes));
        return Utility.convertDTO2JsonString(paystring);
    }

    public SafexResponseDto getFundTransferResponse(String encryptedPayload, String key, String orderId)
            throws ValidationExceptions, JsonMappingException, JsonProcessingException {
        String decryptedPayload = Encryption.decrypt(encryptedPayload, key);
        ObjectMapper ObjMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SafexResponseDto resPayload = ObjMapper.readValue(decryptedPayload, SafexResponseDto.class);
        if (resPayload == null) {
            logger.error("FundTransferResponse RespPayload not found");
            throw new ValidationExceptions(SAFEX_RESP_FAILED, FormValidationExceptionEnums.ES1001);
        }
        if (resPayload.getHeader() == null) {
            logger.error(" FundTransferResponse RespPayload.header not found");
            throw new ValidationExceptions(SAFEX_RESP_FAILED, FormValidationExceptionEnums.ES1001);
        }
        saveDetails.saveSafexRequest(orderId, null, Utility.convertDTO2JsonString(resPayload), null, null,
                null, null, resPayload.getHeader().getRequestId(), null,
                null, null, "FUNDRES");
        logger.info("FUNDRES API::" + orderId + "|" + Utility.convertDTO2JsonString(resPayload));
        saveDetails.savePayloadBean(resPayload.getPayOutBean());
        return resPayload;
    }

    // -----------------------------------------KEY --------------------------------
    public String getApiSessionKey(PgDetails pgDetails, String orderId) throws JsonProcessingException {
        String pgkey = pgDetails.getPgConfigKey();
        String secret = pgDetails.getPgConfigSecret();
        Header header = new Header();
        header.setOperatingSystem("ANDROID");
        header.setSessionId(pgkey);
        header.setVersion("1.0.0");
        Transaction transaction = new Transaction();
        transaction.setRequestSubType("ONBO");
        transaction.setRequestType("CHECK");
        transaction.setChannel("WEB");
        transaction.setTranCode("0");
        transaction.setTxnAmt("0.0");
        ApiVersionCheck apiVersionCheck = new ApiVersionCheck();
        apiVersionCheck.setHeader(header);
        apiVersionCheck.setTransaction(transaction);
        ServiceReqRes serviceReqRes = new ServiceReqRes();
        System.out.println(apiVersionCheck);
        String apicheck = Utility.convertDTO2JsonString(apiVersionCheck);
        logger.info("FUNDSESSION API::" + orderId + "|" + apicheck);
        saveDetails.saveSafexRequest(orderId, null, null, apicheck, null,
                null, null, null, null,
                null, null, "APIINIT");
        String paystring = Encryption.encrypt(apicheck, secret);
        logger.info("FUNDSESSION API ENC::" + orderId + "|" + paystring);
        serviceReqRes.setPayload(paystring);
        serviceReqRes.setUId("CHECK");
        logger.info("FUNDSESSION REQ::" +  Utility.convertDTO2JsonString(serviceReqRes));
        // System.out.println(Utility.convertDTO2JsonString(serviceReqRes));
        return Utility.convertDTO2JsonString(serviceReqRes);
    }

    public String getApiSessionKey(ServiceRes responseSafex, PgDetails pgDetails, String orderId)
            throws JsonMappingException, JsonProcessingException, ValidationExceptions {

        logger.info("getApiSessionKey");
        logger.info("getApiSessionKey Payload response::"+ Utility.convertDTO2JsonString(responseSafex));
        logger.info("getApiSessionKey Payload::"+ (responseSafex.getPayload().trim()));
        logger.info("getApiSessionKey Secret::"+ (pgDetails.getPgConfigSecret()));
        String decryptedPayload = Encryption.decrypt(responseSafex.getPayload().trim(), pgDetails.getPgConfigSecret());
        logger.info("getApiSessionKey decryptedPayload::"+ decryptedPayload);
        ObjectMapper ObjMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ResPayload resPayload = ObjMapper.readValue(decryptedPayload, ResPayload.class);
        if (resPayload == null) {
            logger.error("RespPayload not found");
            throw new ValidationExceptions(SAFEX_RESP_FAILED, FormValidationExceptionEnums.ES1001);
        }
        if (resPayload.getHeader() == null) {
            logger.error("RespPayload.header not found");
            throw new ValidationExceptions(SAFEX_RESP_FAILED, FormValidationExceptionEnums.ES1001);
        }
        logger.info("FUNDSESSION API RES::" + orderId + "|" + Utility.convertDTO2JsonString(resPayload));
        saveDetails.saveSafexRequest(orderId, null, null, null, Utility.convertDTO2JsonString(resPayload),
                null, null, null, resPayload.getHeader().getRequestId(),
                null, resPayload.getHeader().getKey(), "APIRES");
        // System.out.println(resPayload.getHeader().getKey());
        return resPayload.getHeader().getKey();
    }

    public String getStatusApiSessionKey(PgDetails pgDetails, String orderId) throws JsonProcessingException {
        String pgkey = pgDetails.getPgConfigKey();
        String secret = pgDetails.getPgConfigSecret();
        Header header = new Header();
        header.setOperatingSystem("ANDROID");
        header.setSessionId(pgkey);
        header.setVersion("1.0.0");
        Transaction transaction = new Transaction();
        transaction.setRequestSubType("ONBO");
        transaction.setRequestType("CHECK");
        transaction.setChannel("WEB");
        transaction.setTranCode("0");
        transaction.setTxnAmt("0.0");
        ApiVersionCheck apiVersionCheck = new ApiVersionCheck();
        apiVersionCheck.setHeader(header);
        apiVersionCheck.setTransaction(transaction);
        ServiceReqRes serviceReqRes = new ServiceReqRes();
        String apicheck = Utility.convertDTO2JsonString(apiVersionCheck);
        logger.info("STATUS API::" + orderId + "|" + apicheck);
        String paystring = Encryption.encrypt(apicheck, secret);
        logger.info("STATUS API ENC::" + orderId + "|" + apicheck);
        serviceReqRes.setPayload(paystring);
        serviceReqRes.setUId("CHECK");
        // System.out.println(Utility.convertDTO2JsonString(serviceReqRes));

        return Utility.convertDTO2JsonString(serviceReqRes);
    }

    public String getStatusApiSessionKey(ServiceRes responseSafex, PgDetails pgDetails, String orderId)
            throws JsonMappingException, JsonProcessingException, ValidationExceptions {

        logger.info("getApiSessionKey");
        String decryptedPayload = Encryption.decrypt(responseSafex.getPayload(), pgDetails.getPgConfigKey());
        ObjectMapper ObjMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ResPayload resPayload = ObjMapper.readValue(decryptedPayload, ResPayload.class);
        if (resPayload == null) {
            logger.error("RespPayload not found");
            throw new ValidationExceptions(SAFEX_RESP_FAILED, FormValidationExceptionEnums.ES1001);
        }
        if (resPayload.getHeader() == null) {
            logger.error("RespPayload.header not found");
            throw new ValidationExceptions(SAFEX_RESP_FAILED, FormValidationExceptionEnums.ES1001);
        }
        logger.info("STATUS API RES::" + orderId + "|" + Utility.convertDTO2JsonString(resPayload));
        // System.out.println(resPayload.getHeader().getKey());
        return resPayload.getHeader().getKey();
    }
}
