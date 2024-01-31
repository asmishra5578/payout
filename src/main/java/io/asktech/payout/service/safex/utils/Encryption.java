package io.asktech.payout.service.safex.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.asktech.payout.service.safex.dto.ApiVersionCheck;
import io.asktech.payout.service.safex.dto.Header;
import io.asktech.payout.service.safex.dto.PayOutBean;
import io.asktech.payout.service.safex.dto.ResPayload;
import io.asktech.payout.service.safex.dto.SafexRequestDto;
import io.asktech.payout.service.safex.dto.ServiceReqRes;
import io.asktech.payout.service.safex.dto.ServiceRes;
import io.asktech.payout.service.safex.dto.Transaction;
import io.asktech.payout.utils.Utility;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

public class Encryption {
    public static void main(String[] args) throws JsonProcessingException {
        SafexRequestDto safexRequestDto = new SafexRequestDto();
        safexRequestDto.setHeader(createHeader());
        PayOutBean payOutBean = new PayOutBean();
        payOutBean.setAccountHolderName("pRABEER");
        payOutBean.setAccountNo("72367236434");
        payOutBean.setAccountType("Saving");
        payOutBean.setBankName("Kotak");
        payOutBean.setIfscCode("KKBK0000291");
        payOutBean.setMobileNo("9903947078");
        payOutBean.setTxnAmount("2");
        payOutBean.setTxnType("IMPS");
        safexRequestDto.setPayOutBean(payOutBean);
        safexRequestDto.setTransaction(createTransaction());
        String key = getApiSessionKey();

        createPaymentServiceRequest(Utility.convertDTO2JsonString(safexRequestDto), key);
        // System.out.println(Utility.convertDTO2JsonString(serviceReqRes));
    }

    public static String getApiSessionKey() throws JsonProcessingException {
        Header header = createSessionHeader();
        Transaction transaction = createSessionTransaction();
        ApiVersionCheck apiVersionCheck = new ApiVersionCheck();
        apiVersionCheck.setHeader(header);
        apiVersionCheck.setTransaction(transaction);
        System.out.println();
        ServiceReqRes serviceReqRes = createServiceRequest(Utility.convertDTO2JsonString(apiVersionCheck));
        System.out.println(Utility.convertDTO2JsonString(serviceReqRes));
        HttpResponse<String> response = Unirest.post("https://payout.safexpay.com/agWalletAPI/v2/agg")
                .body(Utility.convertDTO2JsonString(serviceReqRes)).asString();
        String res = response.getBody();
        ObjectMapper mapper = new ObjectMapper();
        ServiceRes serviceRes = mapper.readValue(res, ServiceRes.class);
        String decryptedPayload = (decrypt(serviceRes.getPayload(), "9v6ZyFBzNYoP2Un8H5cZq5FeBwxL6itqNZsm7lisGBQ="));
        ObjectMapper ObjMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ResPayload resPayload = ObjMapper.readValue(decryptedPayload, ResPayload.class);
        System.out.println(resPayload.getHeader().getKey());
        return resPayload.getHeader().getKey();
    }

    public static Header createHeader() {
        Header header = new Header();
        header.setOperatingSystem("WEB");
        header.setSessionId("DIST1334534628");

        return header;
    }

    public static Header createSessionHeader() {
        Header header = new Header();
        header.setOperatingSystem("ANDROID");
        header.setSessionId("DIST1334534628");
        header.setVersion("1.0.0");

        return header;
    }

    public static Transaction createTransaction() {
        Transaction transaction = new Transaction();
        transaction.setRequestSubType("PWTB");
        transaction.setRequestType("WTW");
        return transaction;
    }

    public static Transaction createSessionTransaction() {
        Transaction transaction = new Transaction();
        transaction.setRequestSubType("ONBO");
        transaction.setRequestType("CHECK");
        transaction.setChannel("WEB");
        transaction.setTranCode("0");
        transaction.setTxnAmt("0.0");
        return transaction;
    }

    public static ServiceReqRes createServiceRequest(String payload) throws JsonProcessingException {
        ServiceReqRes serviceReqRes = new ServiceReqRes();
        System.out.println(payload);
        String paystring = encrypt(payload, "9v6ZyFBzNYoP2Un8H5cZq5FeBwxL6itqNZsm7lisGBQ=");
        System.out.println(paystring);
        // serviceReqRes.setAgId("DIST1334534628");
        // serviceReqRes.setMId("DIST1334534628");
        serviceReqRes.setPayload(paystring);
        serviceReqRes.setUId("CHECK");
        return serviceReqRes;
    }

    public static ServiceReqRes createPaymentServiceRequest(String payload, String key) throws JsonProcessingException {
        ServiceReqRes serviceReqRes = new ServiceReqRes();
        System.out.println(payload);
        String paystring = encrypt(payload, key);
        System.out.println(paystring);
        //serviceReqRes.setAgId("DIST1334534628");
      //  serviceReqRes.setMId("DIST1334534628");
        serviceReqRes.setPayload(paystring);
        serviceReqRes.setUId("DIST1334534628");
        System.out.println(Utility.convertDTO2JsonString(serviceReqRes));
        HttpResponse<String> response = Unirest.post("https://payout.safexpay.com/agWalletAPI/v2/agg")
                .body(Utility.convertDTO2JsonString(serviceReqRes)).asString();
        ObjectMapper ObjMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ServiceRes resPayload = ObjMapper.readValue(response.getBody(), ServiceRes.class);
        String data = decrypt(resPayload.getPayload(), key);
        System.out.println(data);
        return serviceReqRes;
    }

    private static final String ENCRYPTION_IV = "0123456789abcdef";
    private static final String PADDING = "AES/CBC/PKCS5Padding";
    private static final String ALGORITHM = "AES";
    private static final String CHART_SET = "UTF-8";

    public static String sha256Checksum(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
            StringBuffer hexString = new StringBuffer();
            for (byte byteHash : hash) {
                String hex = Integer.toHexString(0xff & byteHash);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String encrypt(final String textToEncrypt, final String key) {
        try {
            final Cipher cipher = Cipher.getInstance(PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, makeKey(key), makeIv());

            return new String(Base64.getEncoder().encode(cipher.doFinal(textToEncrypt.getBytes())));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(final String textToDecrypt, final String key) {
        try {
            final Cipher cipher = Cipher.getInstance(PADDING);
            cipher.init(Cipher.DECRYPT_MODE, makeKey(key), makeIv());

            return new String(cipher.doFinal(Base64.getDecoder().decode(textToDecrypt)));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static AlgorithmParameterSpec makeIv() {
        try {
            return new IvParameterSpec(ENCRYPTION_IV.getBytes(CHART_SET));
        } catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Key makeKey(final String encryptionKey) {
        try {
            final byte[] key = Base64.getDecoder().decode(encryptionKey);
            return new SecretKeySpec(key, ALGORITHM);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String generateMerchantKey() {
        String newKey = null;
        try {

            final KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(256);
            final SecretKey skey = kgen.generateKey();
            final byte[] raw = skey.getEncoded();
            newKey = new String(Base64.getEncoder().encode(raw));
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return newKey;
    }

    public String value(String b) {

        return b;

    }

}