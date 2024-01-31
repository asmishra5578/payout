package io.asktech.payout.service.kitzone;

// import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.asktech.payout.dto.merchant.AccountTransferMerReq;
import io.asktech.payout.dto.merchant.AccountTransferUPIMerReq;
import io.asktech.payout.dto.merchant.TransactionResponseMerRes;
import io.asktech.payout.dto.reqres.TransferStatusReq;
import io.asktech.payout.dto.reqres.WalletTransferRes;
import io.asktech.payout.modal.merchant.PgDetails;
import io.asktech.payout.modal.merchant.TransactionDetails;
import io.asktech.payout.service.IProcessor;
import io.asktech.payout.service.kitzone.dto.AdditionalDetails;
import io.asktech.payout.service.kitzone.dto.EncryptedResponseDto;
import io.asktech.payout.service.kitzone.dto.PaymentReqDto;
import io.asktech.payout.service.kitzone.dto.PayoutRes;
import io.asktech.payout.service.kitzone.dto.ResultDtIn;
import io.asktech.payout.service.kitzone.dto.ResultN;
import io.asktech.payout.service.kitzone.dto.ResultOrderId;
// import io.asktech.payout.service.kitzone.dto.StatusResDto;
import io.asktech.payout.service.kitzone.model.KitzoneModel;
import io.asktech.payout.service.kitzone.repo.KitzoneRepo;

@Service
public class KitzoneProessor implements IProcessor {

    Logger logger = LoggerFactory.getLogger(KitzoneProessor.class);

    /* Start Autowired Section */

    @Autowired
    KitzoneUtility kitzoneUtility;
    @Autowired
    KitzoneRepo kitzoneRepo;
    /* End Autowired Section */

    @Override
    public WalletTransferRes doAccountTransfer(AccountTransferMerReq dto, String merchantid, PgDetails pg)
            throws JsonProcessingException {
        WalletTransferRes walletTransferRes = new WalletTransferRes();
        walletTransferRes.setPgId(pg.getPgId());
        walletTransferRes.setStatus("PENDING");
        walletTransferRes.setStatusMessage("PENDING");
        logger.info("\n INSIDE THE KITZONE\n");

        KitzoneModel kitzoneModel = new KitzoneModel();
        PayoutRes res = kitzoneUtility.makePayment(dto, merchantid, pg, kitzoneModel);
/* 
        if (res == null) {
            logger.info("\n Something went wrong\n");
            walletTransferRes.setStatus("PENDING");
            walletTransferRes.setErrorMsg("SOME ERROR in MAKE PAYMENT");
        } else {
            logger.info("\n ALL is fine here\n");
            walletTransferRes.setErrorMsg(res.getResultDt().getBankResponse().getMESSAGE());
            walletTransferRes.setReferenceId(res.getResultDt().getTransactionID());
            walletTransferRes.setStatusMessage(res.getResultDt().getBankResponse().getMESSAGE());
            walletTransferRes.setStatus("PENDING");
            if (res.getResultDt().getBankResponse().getSTATUS().equals("SUCCESS")) {
                walletTransferRes.setStatus("PENDING");
            }
        }*/

        if (res == null) {
            logger.info("\n Something went wrong\n");
            walletTransferRes.setStatus("PENDING");
            walletTransferRes.setErrorMsg("SOME ERROR in MAKE PAYMENT");
        } else {
            logger.info("\n ALL is fine here\n");
            walletTransferRes.setErrorMsg(res.getResultDt().getMessage());
            walletTransferRes.setReferenceId(res.getTransactionId());
            walletTransferRes.setStatusMessage(res.getResultDt().getMessage());
            walletTransferRes.setStatus("PENDING");
            if (res.getResultDt().getStatus().equals("SUCCESS")) {
                walletTransferRes.setStatus("PENDING");
            }}

        return walletTransferRes;

    }

    @Override
    public TransactionResponseMerRes doWalletTransferStatus(TransferStatusReq dto, String merchantid,
            TransactionDetails transactionDetails, PgDetails pg) throws JsonProcessingException {

        ObjectMapper obMapper = new ObjectMapper();

        logger.info("\n  Kitzone transaction status\n");
        TransactionResponseMerRes walletTransferRes = new TransactionResponseMerRes();
        KitzoneModel modelOp = kitzoneRepo.findByOrderId(dto.getOrderId());
        if (modelOp == null) {
            logger.info("\n  order id not found in kitzone :" + dto.getOrderId() + " \n");
            // walletTransferRes.set("ORDER ID NOT FOUND IN KITZOne");
            walletTransferRes.setStatus("PENDING");
            walletTransferRes.setErrorMsg("order id not found in kitzone");
            return walletTransferRes;
        }
        // ResultDtIn res = kitzoneUtility.checkStatus(modelOp, pg);
        ResultN res= kitzoneUtility.checkStatus(modelOp, pg);
        // ResultOrderId res = kitzoneUtility.checkStatusByOrderId(modelOp, pg, transactionDetails);

        if (res == null) {
            walletTransferRes.setStatus("PENDING");
            return walletTransferRes;
        }

/*

        if (res.getStatus() != 0) {
            walletTransferRes.setStatus("PENDING");
            modelOp.setStatus(String.valueOf(res.getStatus()));
            if (res.getAdditionalDetails() != null) {
                if (res.getStatus() == 2) {
                    AdditionalDetails additionalDetails = obMapper.readValue(res.getAdditionalDetails(),
                            AdditionalDetails.class);
                    if (additionalDetails.getErrorMessage() != null) {
                        modelOp.setErrorString(additionalDetails.getErrorMessage());
                        walletTransferRes.setStatusMessage(additionalDetails.getErrorMessage());
                        walletTransferRes.setStatus("PENDING");
                        walletTransferRes.setErrorMsg(additionalDetails.getErrorMessage());
                    }
                }
                if (res.getStatus() == 1) {

                    AdditionalDetails additionalDetails = obMapper.readValue(res.getAdditionalDetails(),
                            AdditionalDetails.class);

                    if (additionalDetails.getErrorMessage() != null) {
                        modelOp.setErrorString(additionalDetails.getErrorMessage());
                        walletTransferRes.setStatusMessage(additionalDetails.getErrorMessage());
                        walletTransferRes.setStatus("PENDING");
                        walletTransferRes.setErrorMsg(additionalDetails.getErrorMessage());
                    }
                }
            }
            // modelOp.setStatus("PENDING");
            kitzoneRepo.save(modelOp);
            return walletTransferRes;
        } else {

            logger.info("\n" + res.getAdditionalDetails() + "\n");
            AdditionalDetails additionalDetails = obMapper.readValue(res.getAdditionalDetails(),
                    AdditionalDetails.class);
            logger.info("\n" + additionalDetails.getBANKRRN() + "\n");

            if ((additionalDetails.getBANKRRN().length() > 5) && (res.getStatus() == 0)) {
                walletTransferRes.setMessage(res.getDetail());
                walletTransferRes.setUtrId(additionalDetails.getUtrNumber());
                walletTransferRes.setStatusMessage(additionalDetails.getBANKRRN() + " :  " + additionalDetails.getUtrNumber());
                walletTransferRes.setOrderid(dto.getOrderId());
                walletTransferRes.setStatus("SUCCESS");

                modelOp.setUtrId(additionalDetails.getBANKRRN());
                modelOp.setStatus("SUCCESS");
                // modelOp.setPgOrderId(res.getURN());
                kitzoneRepo.save(modelOp);
            }
            modelOp.setStatusMessage(additionalDetails.getBANKRRN());
            kitzoneRepo.save(modelOp);

        }
 */


        /* CODE FOR CHECK STATUS BY EXTERNAL TRANSACTION ID */
        // StatusResDto res = kitzoneUtility.checkStatus(modelOp, pg);
        /* Todo aDD FAILED CODE */
        // if (res.getUTRNUMBER() == null) {
        // // walletTransferRes.setStatus("PENDING");
        // if(res.getSTATUS().equalsIgnoreCase("FAILURE")){
        // walletTransferRes.setStatus("PENDING");
        // modelOp.setStatus(res.getRESPONSE());
        // }
        // kitzoneRepo.save(modelOp);
        // return walletTransferRes;
        // }

        // if ((res.getUTRNUMBER().length() > 5) &&
        // (res.getSTATUS().equalsIgnoreCase("SUCCESS"))) {
        // walletTransferRes.setMessage(res.getSTATUS());
        // walletTransferRes.setUtrId(res.getUTRNUMBER());
        // walletTransferRes.setOrderid(dto.getOrderId());
        // walletTransferRes.setStatus(res.getSTATUS());

        // modelOp.setUtrId(res.getUTRNUMBER());
        // modelOp.setStatus(res.getRESPONSE());
        // modelOp.setPgOrderId(res.getURN());
        // kitzoneRepo.save(modelOp);

        // }
        /* END OF CODE FOR CHECK STATUS BY EXTERNAL TRANSACTION ID */


        /* NEW APICODE FOR CHECK STATUS BY EXTERNAL TRANSACTION ID */
        // StatusResDto res = kitzoneUtility.checkStatus(modelOp, pg);
        /* Todo aDD FAILED CODE */
        // if (res.getData().getTransfer().getUtr() == null) {
        // // walletTransferRes.setStatus("PENDING");
        // if(res.getData().getTransfer().getStatus().equalsIgnoreCase("FAILURE")){
        // walletTransferRes.setStatus("PENDING");
        // modelOp.setStatus(res.getMessage());
        // }
        // kitzoneRepo.save(modelOp);
        // return walletTransferRes;
        // }
           if (res.getTransfer().getReferenceId() == null) {
        // walletTransferRes.setStatus("PENDING");
        if(res.getTransfer().getStatus().equalsIgnoreCase("FAILURE")){
        walletTransferRes.setStatus("PENDING");
        // modelOp.setStatus(res.getTransfer().get);
        }
        kitzoneRepo.save(modelOp);
        return walletTransferRes;
        }

        // if ((res.getData().getTransfer().getUtr().length()  > 5) &&
        // (res.getData().getTransfer().getStatus().equalsIgnoreCase("SUCCESS"))) {
        // walletTransferRes.setMessage(res.getData().getTransfer().getStatus());
        // walletTransferRes.setUtrId(res.getData().getTransfer().getUtr());
        // walletTransferRes.setOrderid(dto.getOrderId());
        // walletTransferRes.setStatus(res.getData().getTransfer().getStatus());

        // modelOp.setUtrId(res.getData().getTransfer().getUtr());
        // modelOp.setStatus(res.getData().getTransfer().getStatus());
        // modelOp.setPgOrderId(res.getData().getTransfer().getBeneId());
        // kitzoneRepo.save(modelOp);

        // }


if ((res.getTransfer().getReferenceId().length()  > 5) &&
        (res.getTransfer().getStatus().equalsIgnoreCase("SUCCESS"))) {
        walletTransferRes.setMessage(res.getTransfer().getStatus());
        walletTransferRes.setUtrId(res.getTransfer().getReferenceId());
        walletTransferRes.setOrderid(dto.getOrderId());
        walletTransferRes.setStatus(res.getTransfer().getStatus());

        modelOp.setUtrId(res.getTransfer().getReferenceId());
        modelOp.setStatus(res.getTransfer().getStatus());
        modelOp.setPgOrderId(res.getTransfer().getBeneId());
        kitzoneRepo.save(modelOp);

        }






        /* END OF CODE FOR CHECK STATUS BY EXTERNAL TRANSACTION ID */


        return walletTransferRes;
    }

    @Override
    public WalletTransferRes doAccountTransferUPI(AccountTransferUPIMerReq dto, String merchantid, PgDetails pg)
            throws JsonProcessingException {

        throw new UnsupportedOperationException("Unimplemented method 'doAccountTransferUPI'");
    }

}
