package io.asktech.payout.service.payg.requests;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.asktech.payout.modal.merchant.ApiRequestResponseLogger;
import io.asktech.payout.modal.merchant.PgDetails;
import io.asktech.payout.repository.reqres.ApiRequestResponseLoggerRepo;
import io.asktech.payout.service.payg.PaygUtil;
import io.asktech.payout.service.payg.dto.AddContact;
import io.asktech.payout.service.payg.dto.AddContactResponse;
import io.asktech.payout.service.payg.dto.FundTransferDto;
import io.asktech.payout.service.payg.dto.FundTransferResponseDto;
import io.asktech.payout.service.payg.dto.TransactionStatusDto;
import io.asktech.payout.service.payg.dto.TransactionStatusResponseDto;
import io.asktech.payout.utils.Utility;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

@Component
public class PaygPaymentRequest {

    @Autowired
    PaygUtil paygUtil;
    @Value("${paygCredentials.addContact}")
    String addContactLink;
    @Value("${paygCredentials.fundTransfer}")
    String fundTransferLink;
    @Value("${paygCredentials.statusCheck}")
    String statusCheckLink;
    @Autowired
    ApiRequestResponseLoggerRepo apiRequestResponseLoggerRepo;

    static Logger logger = LoggerFactory.getLogger(PaygPaymentRequest.class);

    public AddContactResponse addContact(AddContact contact, PgDetails pgDetails, String merchantid)
            throws JsonProcessingException {
        String signature = paygUtil.getSignature(pgDetails);
        logger.info("addContact request" + Utility.convertDTO2JsonString(contact));
        ApiRequestResponseLogger apiRequestResponseLogger = new ApiRequestResponseLogger();
        apiRequestResponseLogger.setMerchantId(merchantid);
        apiRequestResponseLogger.setRequestId(contact.getRequestUniqueId());
        apiRequestResponseLogger.setRequest(Utility.convertDTO2JsonString(contact));
        apiRequestResponseLogger.setServiceProvider("PAYG");
        apiRequestResponseLogger.setApiType("addContact");
        // apiRequestResponseLoggerRepo.save(apiRequestResponseLogger);
        logger.info(Utility.convertDTO2JsonString(apiRequestResponseLoggerRepo.save(apiRequestResponseLogger)));

        HttpResponse<AddContactResponse> response = Unirest.post(addContactLink)
                .header("Authorization", "basic " + signature)
                .header("Connection", "keep-alive")
                .header("Content-Type", "application/json")
                .body(contact).asObject(AddContactResponse.class)
                .ifFailure(Object.class, r -> {
                    Object e = r.getBody();
                    try {
                        logger.info("Contact Response error::" + Utility.convertDTO2JsonString(e));
                        apiRequestResponseLogger.setErrorStatus(Utility.convertDTO2JsonString(e));
                        apiRequestResponseLoggerRepo.save(apiRequestResponseLogger);
                    } catch (JsonProcessingException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                });

        logger.info("addContact response::" + Utility.convertDTO2JsonString(response.getBody()));
        apiRequestResponseLogger.setResponse(Utility.convertDTO2JsonString(response.getBody()));
        apiRequestResponseLoggerRepo.save(apiRequestResponseLogger);

        return response.getBody();
    }

    public FundTransferResponseDto fundTransfer(FundTransferDto fundTransfer, PgDetails pgDetails, String merchantid)
            throws JsonProcessingException {
        String signature = paygUtil.getSignature(pgDetails);

        ApiRequestResponseLogger apiRequestResponseLogger = new ApiRequestResponseLogger();
        apiRequestResponseLogger.setMerchantId(merchantid);
        apiRequestResponseLogger.setRequestId(fundTransfer.getUniqueRequestId());
        apiRequestResponseLogger.setRequest(Utility.convertDTO2JsonString(fundTransfer));
        apiRequestResponseLogger.setServiceProvider("PAYG");
        apiRequestResponseLogger.setApiType("fundTransfer");
        apiRequestResponseLoggerRepo.save(apiRequestResponseLogger);

        HttpResponse<FundTransferResponseDto> response = Unirest.post(fundTransferLink)
                .header("Authorization", "basic " + signature)
                .header("Connection", "keep-alive")
                .header("Content-Type", "application/json")
                .body(fundTransfer).asObject(FundTransferResponseDto.class)
                .ifFailure(Object.class, r -> {
                    Object e = r.getBody();
                    try {
                        logger.info("Fundtransfer Response error::" + Utility.convertDTO2JsonString(e));
                        apiRequestResponseLogger.setErrorStatus(Utility.convertDTO2JsonString(e));
                        apiRequestResponseLoggerRepo.save(apiRequestResponseLogger);
                    } catch (JsonProcessingException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                });
        logger.info("fundTransfer response::" + Utility.convertDTO2JsonString(response.getBody()));
        apiRequestResponseLogger.setResponse(Utility.convertDTO2JsonString(response.getBody()));
        apiRequestResponseLoggerRepo.save(apiRequestResponseLogger);
        return response.getBody();
    }

    public TransactionStatusResponseDto transactionStatus(TransactionStatusDto transactionStatus, PgDetails pgDetails,
            String merchantid) throws JsonProcessingException {
        String signature = paygUtil.getSignature(pgDetails);

        ApiRequestResponseLogger apiRequestResponseLogger = new ApiRequestResponseLogger();
        apiRequestResponseLogger.setMerchantId(merchantid);
        apiRequestResponseLogger.setRequestId(transactionStatus.getTransactionId());
        apiRequestResponseLogger.setRequest(Utility.convertDTO2JsonString(transactionStatus));
        apiRequestResponseLogger.setServiceProvider("PAYG");
        apiRequestResponseLogger.setApiType("TransactionApi");
        apiRequestResponseLoggerRepo.save(apiRequestResponseLogger);
        logger.info("transactionStatus Request::" + Utility.convertDTO2JsonString(transactionStatus));
        try {
            HttpResponse<TransactionStatusResponseDto> response = Unirest.post(statusCheckLink)
                    .header("Authorization", "basic " + signature)
                    .header("Connection", "keep-alive")
                    .header("Content-Type", "application/json")
                    .body(transactionStatus).asObject(TransactionStatusResponseDto.class)
                    .ifFailure(Object.class, r -> {
                        Object e = r.getBody();
                        try {
                            logger.info("Status Response error::" + Utility.convertDTO2JsonString(e));
                            apiRequestResponseLogger.setErrorStatus(Utility.convertDTO2JsonString(e));
                            apiRequestResponseLoggerRepo.save(apiRequestResponseLogger);
                        } catch (JsonProcessingException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    });
            logger.info("transactionStatus response::" + Utility.convertDTO2JsonString(response.getBody()));
            apiRequestResponseLogger.setResponse(Utility.convertDTO2JsonString(response.getBody()));
            apiRequestResponseLoggerRepo.save(apiRequestResponseLogger);
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
