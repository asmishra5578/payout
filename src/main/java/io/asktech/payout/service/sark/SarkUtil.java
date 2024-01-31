package io.asktech.payout.service.sark;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.asktech.payout.dto.merchant.AccountTransferMerReq;
import io.asktech.payout.dto.merchant.AccountTransferUPIMerReq;
import io.asktech.payout.exceptions.ValidationExceptions;
import io.asktech.payout.modal.merchant.PgDetails;
import io.asktech.payout.modal.merchant.TransactionDetails;
import io.asktech.payout.service.sark.dto.SarkStatusRequest;
import io.asktech.payout.service.sark.dto.SarkTrasactionRequest;
import io.asktech.payout.service.sark.dto.SarkUpiTransactionRequest;
import io.asktech.payout.service.sark.models.SarkReqRes;
import io.asktech.payout.service.sark.repo.SarkReqResRepo;
import io.asktech.payout.utils.Utility;
import io.asktech.payout.utils.BankIfsc.BankService;

@Component
public class SarkUtil {
    @Autowired
    SarkReqResRepo sarkReqResRepo;
    @Autowired
    BankService bankService;
    static Logger logger = LoggerFactory.getLogger(SarkUtil.class);

    public SarkTrasactionRequest generateTransferRequest(AccountTransferMerReq dto, String merchantid, PgDetails pg) {
        String orderid = dto.getInternalOrderid();
        SarkTrasactionRequest sarkTrasactionRequest = new SarkTrasactionRequest();
        sarkTrasactionRequest.setUser_id(pg.getPgConfig1());
        sarkTrasactionRequest.setApi_token(pg.getPgConfigKey());
        sarkTrasactionRequest.setKey(pg.getPgConfigSecret());
        if (dto.getBeneficiaryName().length() > 29) {
            sarkTrasactionRequest.setBeneName(dto.getBeneficiaryName().substring(0, 29).trim());
        } else {
            sarkTrasactionRequest.setBeneName(dto.getBeneficiaryName().trim());
        }
        sarkTrasactionRequest.setBeneName(dto.getBeneficiaryName());
        String txnMode = ((dto.getRequestType().equals("IMPS")) ? "2" : "1");
        sarkTrasactionRequest.setTxnMode(txnMode);
        sarkTrasactionRequest.setMobile(dto.getPhonenumber());
        String[] benef = dto.getBeneficiaryName().trim().split(" ");
        logger.info("BeneSark Name::" + dto.getBeneficiaryName());
        if (benef.length == 1) {
            sarkTrasactionRequest.setSenderFirstName(dto.getBeneficiaryName());
            sarkTrasactionRequest.setSenderLastName("N A");
        } else {
            String[] benefirst = Arrays.copyOf(benef, benef.length - 1);
            logger.info("BeneSark Name::" + benefirst);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < benefirst.length; i++) {
                stringBuilder.append(benefirst[i] + " ");
            }
            String benefi = stringBuilder.toString().trim();
            if (benefi.length() > 29) {
                benefi = benefi.substring(0, 29);
            }
            sarkTrasactionRequest.setSenderFirstName(benefi.trim());
            logger.info("BeneSark First Name::" + benefi);
            if (benef[(benef.length) - 1] != null && benef[(benef.length) - 1].length() < 4) {
                sarkTrasactionRequest.setSenderLastName("N A");
                logger.info("BeneSark Last Name::" + "N A");
            } else {
                if (benef[(benef.length) - 1].length() > 29) {
                    sarkTrasactionRequest.setSenderLastName(benef[(benef.length) - 1].substring(0, 29).trim());
                } else {
                    sarkTrasactionRequest.setSenderLastName(benef[(benef.length) - 1].trim());
                }
                logger.info("BeneSark Last Name::" + benef[(benef.length) - 1]);
            }

        }

        sarkTrasactionRequest.setBankName(bankService.getBankName(dto.getIfsc()));

        sarkTrasactionRequest.setAccountNumber(dto.getBankaccount());
        sarkTrasactionRequest.setIfsc(dto.getIfsc());
        sarkTrasactionRequest.setUniqueRefId(orderid.replaceAll("[^0-9.]", "").substring(orderid.length() - 20));
        if (dto.getAmount().contains(".")) {
            sarkTrasactionRequest.setTxnAmount(dto.getAmount().split("\\.")[0]);
        } else {
            sarkTrasactionRequest.setTxnAmount(dto.getAmount());
        }
        sarkTrasactionRequest.setEnd_point_ip("66.249.66.24");
        sarkTrasactionRequest.setLongitude("41.40338");
        sarkTrasactionRequest.setLatitude("2.17403");
        return sarkTrasactionRequest;
    }

    public SarkUpiTransactionRequest generateUpiTransferRequest(AccountTransferUPIMerReq dto, String merchantid,
            PgDetails pg) {
        try {
            logger.info("SARK AccountTransferUPIMerReq::" + Utility.convertDTO2JsonString(dto));
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String orderid = dto.getInternalOrderid();
        SarkUpiTransactionRequest sarkTrasactionRequest = new SarkUpiTransactionRequest();
        sarkTrasactionRequest.setUser_id(pg.getPgConfig1());
        sarkTrasactionRequest.setApi_token(pg.getPgConfigKey());
        sarkTrasactionRequest.setKey(pg.getPgConfigSecret());
        sarkTrasactionRequest.setSenderMobile(dto.getPhonenumber());
        sarkTrasactionRequest.setSenderName(dto.getBeneficiaryName());
        sarkTrasactionRequest.setBeneName(dto.getBeneficiaryName());
        sarkTrasactionRequest.setUpiId(dto.getBeneficiaryVPA());
        sarkTrasactionRequest.setUniqueRefId(orderid.replaceAll("[^0-9.]", "").substring(orderid.length() - 20));
        if (dto.getAmount().contains(".")) {
            sarkTrasactionRequest.setTxnAmount(dto.getAmount().split("\\.")[0]);
        } else {
            sarkTrasactionRequest.setTxnAmount(dto.getAmount());
        }
        sarkTrasactionRequest.setEnd_point_ip("66.249.66.24");
        sarkTrasactionRequest.setLongitude("41.40338");
        sarkTrasactionRequest.setLatitude("2.17403");
        try {
            logger.info("SARK sarkTrasactionRequest::" + Utility.convertDTO2JsonString(sarkTrasactionRequest));
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sarkTrasactionRequest;
    }

    public SarkStatusRequest generateStatusRequest(TransactionDetails dto, String merchantid, PgDetails pg,
            String uniqueRefId) throws ParseException, ValidationExceptions {
        String orderid = dto.getInternalOrderId();
        SarkReqRes sarkReqRes = sarkReqResRepo.findByOrderId(orderid);
        Date crdatetxn = null;
        if (sarkReqRes.getTxnTime() == null) {
            crdatetxn = sarkReqRes.getCreated();
        } else {
            try {
                crdatetxn = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(sarkReqRes.getTxnTime());
            } catch (Exception e) {
                logger.error(e.getMessage());
                crdatetxn = sarkReqRes.getCreated();
            }
            logger.info("crdatetxn::" + crdatetxn.toString());
        }

        Date crdate = crdatetxn;
        // Date crdate = crdatetxn;
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String dt = simpleDateFormat.format(crdate);
        SarkStatusRequest sarkStatusRequest = new SarkStatusRequest();
        sarkStatusRequest.setUser_id(pg.getPgConfig1());
        sarkStatusRequest.setApi_token(pg.getPgConfigKey());
        sarkStatusRequest.setKey(pg.getPgConfigSecret());
        sarkStatusRequest.setUniqueRefId(uniqueRefId);
        sarkStatusRequest.setTxnDate(dt);
        return sarkStatusRequest;
    }
}
