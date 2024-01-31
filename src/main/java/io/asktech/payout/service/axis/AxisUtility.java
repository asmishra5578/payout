package io.asktech.payout.service.axis;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

// import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
// import javax.crypto.IllegalBlockSizeException;
// import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.asktech.payout.dto.merchant.AccountTransferMerReq;
import io.asktech.payout.modal.merchant.PgDetails;
import io.asktech.payout.service.axis.dto.BeneficiaryRegistrationRequest;
import io.asktech.payout.service.axis.dto.BeneficiaryRegistrationRequestBody;
import io.asktech.payout.service.axis.dto.Beneinsert;
import io.asktech.payout.service.axis.dto.BenificaryDto;
import io.asktech.payout.service.axis.dto.FundtransferDto;
import io.asktech.payout.service.axis.dto.GetStatusRequest;
import io.asktech.payout.service.axis.dto.GetStatusRequestBody;
import io.asktech.payout.service.axis.dto.GetStatusResponseBody;
import io.asktech.payout.service.axis.dto.PaymentDetail;
import io.asktech.payout.service.axis.dto.StatusRequestDto;
import io.asktech.payout.service.axis.dto.SubHeader;
import io.asktech.payout.service.axis.dto.TransferPaymentRequest;
import io.asktech.payout.service.axis.dto.TransferPaymentRequestBody;
import io.asktech.payout.service.axis.model.AxisBeneficiary;
import io.asktech.payout.service.axis.model.AxisUtilityModel;
import io.asktech.payout.service.axis.repo.AxisBeneficaryRepo;
import io.asktech.payout.service.axis.repo.AxisUtilityRepo;
import io.asktech.payout.utils.Utility;

@Component
public class AxisUtility {

    static Logger logger = LoggerFactory.getLogger(AxisUtility.class);


    @Autowired
    AxisBeneficaryRepo axisBeneficaryRepo;
    @Autowired
    AxisUtilityRepo axisUtilityRepo;
    @Autowired
    AxisProcessor axisProcessor;

    public BenificaryDto addBenificary(AccountTransferMerReq dto, String merchantid, PgDetails pg, String customerUniqueId) throws JsonProcessingException {


        
        AxisBeneficiary axisBeneficarymodel = new AxisBeneficiary();
        axisBeneficarymodel.setBeneCode(customerUniqueId);
        axisBeneficarymodel.setBeneBankAccount(dto.getBankaccount());
        axisBeneficarymodel.setBeneName(dto.getBeneficiaryName());
        axisBeneficarymodel.setBeneIfscCode(dto.getIfsc());

        axisBeneficaryRepo.save(axisBeneficarymodel);

        // AxisUtilityModel axisUtilityModel



        BenificaryDto benificaryDto = new BenificaryDto();
        BeneficiaryRegistrationRequest beneficiaryRegistrationRequest = new BeneficiaryRegistrationRequest();
        SubHeader subHeader = new SubHeader();
        BeneficiaryRegistrationRequestBody beneficiaryRegistrationRequestBody = new BeneficiaryRegistrationRequestBody();
        Beneinsert beneinsert = new Beneinsert();
        beneinsert.setApiVersion("1.0");
        beneinsert.setBeneLEI("");
        beneinsert.setBeneCode(customerUniqueId);
        beneinsert.setBeneName(dto.getBeneficiaryName());
        beneinsert.setBeneAccNum(dto.getBankaccount());
        beneinsert.setBeneIfscCode(dto.getIfsc());
        beneinsert.setBeneAcType("");
        beneinsert.setBeneBankName("");
        beneinsert.setBeneAddr1("");
        beneinsert.setBeneAddr2("");
        beneinsert.setBeneAddr3("");
        beneinsert.setBeneCity("");
        beneinsert.setBeneState("");
        beneinsert.setBenePincode("");
        beneinsert.setBeneEmailAddr1("");
        beneinsert.setBeneMobileNo("");

        beneficiaryRegistrationRequestBody.setChannelId(pg.getPgConfig2());
        beneficiaryRegistrationRequestBody.setCorpCode(pg.getPgConfig3());
        // USER ID
        beneficiaryRegistrationRequestBody.setUserId("ACC0336231165149");

        List<Beneinsert> beneinsertList = new ArrayList<>();

        beneinsertList.add(beneinsert);
        beneficiaryRegistrationRequestBody.setBeneinsert(beneinsertList);

        // CREATE CHECKSUM
        String checkSum = encodeCheckSumWithSHA256(createCheckSumBeneficiary(beneficiaryRegistrationRequestBody));
        beneficiaryRegistrationRequestBody.setChecksum(checkSum);

        // ENCRYPT THE REQUESTimport java.util.Arrays;
        String encryptedRequest = encryptCallBack(pg.getPgConfig1(), Utility.convertDTO2JsonString(beneficiaryRegistrationRequestBody));

        subHeader.setRequestUUID("ABC123");                           //order id
        subHeader.setServiceRequestId("OpenAPI");                // transfer
        subHeader.setServiceRequestVersion("1.0");
        subHeader.setChannelId(pg.getPgConfig2());

        beneficiaryRegistrationRequest.setSubHeader(subHeader);
        // beneficiaryRegistrationRequest.setBeneficiaryRegistrationRequestBody(beneficiaryRegistrationRequestBody);
        beneficiaryRegistrationRequest.setBeneficiaryRegistrationRequestBodyEncrypted(encryptedRequest);
        benificaryDto.setBeneficiaryRegistrationRequest(beneficiaryRegistrationRequest);

        // Encrypt the Request
        return benificaryDto;
    }


    public FundtransferDto fundTransferRequest(String beneCode,AccountTransferMerReq dto,  PgDetails pg) throws JsonProcessingException{

        Dictionary<String, String> txnBook = new Hashtable();

        txnBook.put("NEFT", "NE");
        txnBook.put("RTGS", "RT");
        txnBook.put("IMPS", "PA");
        
        
        
        SubHeader subHeader = new SubHeader();
        subHeader.setRequestUUID("ABC123");
        subHeader.setServiceRequestId("OpenAPI");
        subHeader.setServiceRequestVersion("1.0");
        subHeader.setChannelId(pg.getPgConfig2());
        PaymentDetail paymentDetail = new PaymentDetail();

        // TODO remove hard coded neft.

        // paymentDetail.setTxnPaymode("NE");
        paymentDetail.setTxnPaymode(txnBook.get(dto.getRequestType()));
        paymentDetail.setCustUniqRef(dto.getOrderid());
        
        // TODO THIS VALUE MUST NOT BE HARD CODED
        paymentDetail.setCorpAccNum("922020034999564");
        // paymentDetail.setValueDate("2022-11-8"); 
        paymentDetail.setValueDate(java.time.LocalDate.now().toString());
        paymentDetail.setTxnAmount(dto.getAmount());
        paymentDetail.setBeneLEI("");
        paymentDetail.setBeneName(dto.getBeneficiaryName());
        paymentDetail.setBeneCode(beneCode);
        paymentDetail.setBeneAcType("");
        paymentDetail.setBeneAccNum(dto.getBankaccount());
        paymentDetail.setBeneAddr1("");
        paymentDetail.setBeneAddr2("");
        paymentDetail.setBeneAddr3("");
        paymentDetail.setBeneBankName("");
        paymentDetail.setBaseCode("");
        paymentDetail.setBeneEmailAddr1("");
        paymentDetail.setBeneIfscCode(dto.getIfsc());
        paymentDetail.setBeneMobileNo("");
        paymentDetail.setBeneCity("");
        paymentDetail.setBeneState("");
        paymentDetail.setBenePincode("");
        paymentDetail.setChequeDate("");
        paymentDetail.setChequeNumber("");
        paymentDetail.setEnrichment1("");
        paymentDetail.setEnrichment2("");
        paymentDetail.setEnrichment3("");
        paymentDetail.setEnrichment4("");
        paymentDetail.setEnrichment5("");
        paymentDetail.setPayableLocation("");
        paymentDetail.setPrintLocation("");
        paymentDetail.setProductCode("");
        paymentDetail.setTxnType("");
        paymentDetail.setSenderToReceiverInfo("");
        // paymentDetail.set

        // TODO PLEASE REST OF FIELD TO EMPTY STRING

        TransferPaymentRequestBody transferPaymentRequestBody = new TransferPaymentRequestBody();
        transferPaymentRequestBody.setChannelId(pg.getPgConfig2());
        transferPaymentRequestBody.setCorpCode(pg.getPgConfig3());

        List<PaymentDetail> paymentDetails = new ArrayList<>();
        paymentDetails.add(paymentDetail);
        transferPaymentRequestBody.setPaymentDetails(paymentDetails);
        // String  checkString = "";
        String checkSum = encodeCheckSumWithSHA256(createCheckSumTransferPayment(transferPaymentRequestBody));
        
        transferPaymentRequestBody.setChecksum(checkSum);

        String encryptedRequest = encryptCallBack(pg.getPgConfig1(), Utility.convertDTO2JsonString(transferPaymentRequestBody));


        TransferPaymentRequest transferPaymentRequest = new TransferPaymentRequest();
        transferPaymentRequest.setSubHeader(subHeader);
        transferPaymentRequest.setTransferPaymentRequestBodyEncrypted(encryptedRequest);

        FundtransferDto fundTransferDto = new FundtransferDto();
        fundTransferDto.setTransferPaymentRequest(transferPaymentRequest);
        return fundTransferDto;

    }

    public StatusRequestDto createStatusRequest(String  orderId,  PgDetails pg) throws JsonProcessingException{

        
        SubHeader subHeader = new SubHeader();
        subHeader.setRequestUUID("ABC123");
        subHeader.setServiceRequestId("OpenAPI");
        subHeader.setServiceRequestVersion("1.0");
        subHeader.setChannelId(pg.getPgConfig2());

        GetStatusRequestBody getStatusRequestBody = new GetStatusRequestBody();
        getStatusRequestBody.setChannelId(pg.getPgConfig2());
        getStatusRequestBody.setCorpCode(pg.getPgConfig3());
        getStatusRequestBody.setCrn(orderId);
        // getStatusRequestBody.setCrn("2820012030981");
        
        String checkSum = encodeCheckSumWithSHA256(createCheckSumStatus(getStatusRequestBody));
        getStatusRequestBody.setChecksum(checkSum);
        String encryptedRequest = encryptCallBack(pg.getPgConfig1(), Utility.convertDTO2JsonString(getStatusRequestBody));


        GetStatusRequest getStatusRequest = new GetStatusRequest();
        getStatusRequest.setSubHeader(subHeader);
        getStatusRequest.setGetStatusRequestBodyEncrypted(encryptedRequest);
        // create check sum

        StatusRequestDto statusRequestDto = new StatusRequestDto();

        statusRequestDto.setGetStatusRequest(getStatusRequest);

        return statusRequestDto;

    }


    public void callBackApi(String data) throws JsonMappingException, JsonProcessingException{
        logger.info(data+ "\n");
        ObjectMapper objeMapper = new ObjectMapper();
        GetStatusResponseBody callBackData = objeMapper.readValue(data,GetStatusResponseBody.class );

        if (callBackData.getStatus().equals("S")){
            AxisUtilityModel axisUtilityModel = axisUtilityRepo.findByOrderId(callBackData.getData().getCUR_TXN_ENQ().get(0).getCrn());
            if (axisUtilityModel !=null){
                axisUtilityModel.setPgTransId(callBackData.getData().getCUR_TXN_ENQ().get(0).getTransaction_id());
                axisUtilityModel.setStatus(callBackData.getData().getCUR_TXN_ENQ().get(0).getTransactionStatus());
                axisUtilityModel.setUtrId(callBackData.getData().getCUR_TXN_ENQ().get(0).getUtrNo());
                axisUtilityModel.setCallBackDcrypt(Utility.convertDTO2JsonString(callBackData));
                axisUtilityRepo.save(axisUtilityModel);
            }
            

        }
    }






    public String encodeCheckSumWithSHA256(String data) {
        MessageDigest md;
        StringBuilder sb = new StringBuilder();
        String response = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(data.getBytes(StandardCharsets.UTF_8));
            // Get the hashbytes
            byte[] hashBytes = md.digest();
            // Converthash bytes to hex format
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            response = sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Internal server error");
        }
        return response;

    }


    public String encryptCallBack(String key, String str_resp) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream(key.length() / 2);

        for (int i = 0; i < key.length(); i += 2) {
            String output = key.substring(i, i + 2);
            int decimal = Integer.parseInt(output, 16);
            baos.write(decimal);
        }

        try {
            SecretKeySpec skeySpec = new SecretKeySpec(baos.toByteArray(), "AES");

            byte[] iv1 = new byte[] { (byte) 0x8E, 0x12, 0x39, (byte) 0x9C, 0x07, 0x72, 0x6F, 0x5A, (byte) 0x8E, 0x12,
                    0x39, (byte) 0x9C, 0x07, 0x72, 0x6F, 0x5A };
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv1);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(1, skeySpec, paramSpec);

            byte[] encrypted = cipher.doFinal(str_resp.getBytes("UTF-8"));

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            os.write(iv1);
            os.write(encrypted);
            byte[] encryptedWithIV = os.toByteArray();

            // return new String(Base64.encode(os.toByteArray()));
            String encryptedResult = Base64.getEncoder().encodeToString(encryptedWithIV);
            return encryptedResult;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public String decryptCallBack(String key, String encrypted) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream(key.length() / 2);

        for (int i = 0; i < key.length(); i += 2) {
            String output = key.substring(i, i + 2);
            int decimal = Integer.parseInt(output, 16);
            baos.write(decimal);
        }

        try {
            SecretKeySpec skeySpec = new SecretKeySpec(baos.toByteArray(), "AES");

            // byte[] encryptedIVandTextAsBytes = Base64.decode(encrypted);
            byte[] encryptedIVandTextAsBytes = Base64.getDecoder().decode(encrypted);
            byte[] iv = Arrays.copyOf(encryptedIVandTextAsBytes, 16);
            byte[] ciphertextByte = Arrays.copyOfRange(encryptedIVandTextAsBytes, 16, encryptedIVandTextAsBytes.length);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(2, skeySpec, new IvParameterSpec(iv));
            byte[] decryptedTextBytes = cipher.doFinal(ciphertextByte);

            String original = new String(decryptedTextBytes, "UTF-8");

            return original;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public String createBeneCode(String orderId){


        List<String> characters = Arrays.asList(orderId.split(""));
       Collections.shuffle(characters);
        String afterShuffle = "";
        for (String character : characters)
        {
                      afterShuffle += character;
                }

        return afterShuffle.substring(0,10);
    }

    public String createCheckSumTransferPayment(TransferPaymentRequestBody transferPaymentRequestBody){
        String cheksum  = transferPaymentRequestBody.getChannelId()+transferPaymentRequestBody.getCorpCode()+transferPaymentRequestBody.getPaymentDetails().get(0).getTxnPaymode()+transferPaymentRequestBody.getPaymentDetails().get(0).getCustUniqRef() +transferPaymentRequestBody.getPaymentDetails().get(0).getCorpAccNum() +transferPaymentRequestBody.getPaymentDetails().get(0).getValueDate() +transferPaymentRequestBody.getPaymentDetails().get(0).getTxnAmount()+transferPaymentRequestBody.getPaymentDetails().get(0).getBeneBankName()+transferPaymentRequestBody.getPaymentDetails().get(0).getBeneCode()+transferPaymentRequestBody.getPaymentDetails().get(0).getBeneAccNum() +transferPaymentRequestBody.getPaymentDetails().get(0).getBeneIfscCode();


        return cheksum;
    }

    public String createCheckSumBeneficiary(BeneficiaryRegistrationRequestBody body){

        String checkSumString = body.getChannelId() + body.getCorpCode()+body.getUserId() + body.getBeneinsert().get(0).getApiVersion()+ body.getBeneinsert().get(0).getBeneCode()+ body.getBeneinsert().get(0).getBeneName()+ body.getBeneinsert().get(0).getBeneAccNum()+ body.getBeneinsert().get(0).getBeneIfscCode();



        return checkSumString;
    }


    public String createCheckSumStatus(GetStatusRequestBody body){
        String checkSum = body.getChannelId() + body.getCorpCode() + body.getCrn();
        
        return checkSum;
    }


}

