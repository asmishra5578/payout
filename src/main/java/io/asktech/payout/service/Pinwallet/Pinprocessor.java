package io.asktech.payout.service.Pinwallet;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.asktech.payout.dto.merchant.AccountTransferMerReq;
import io.asktech.payout.dto.merchant.AccountTransferUPIMerReq;
import io.asktech.payout.dto.merchant.TransactionResponseMerRes;
import io.asktech.payout.dto.reqres.TransferStatusReq;
import io.asktech.payout.dto.reqres.WalletTransferRes;
import io.asktech.payout.modal.merchant.PgDetails;
import io.asktech.payout.modal.merchant.TransactionDetails;
import io.asktech.payout.service.IProcessor;
import io.asktech.payout.service.Pinwallet.Repository.Pinwalletrepository;
import io.asktech.payout.service.Pinwallet.dto.Authreq;
import io.asktech.payout.service.Pinwallet.dto.Authres;
import io.asktech.payout.service.Pinwallet.dto.Paymentres;
import io.asktech.payout.service.Pinwallet.dto.StatusReqDto;
import io.asktech.payout.service.Pinwallet.model.Pinwalledpayouttrnxs;
import io.asktech.payout.utils.Utility;

@Service
public class Pinprocessor implements IProcessor {
    static Logger logger = LoggerFactory.getLogger(Pinprocessor.class);
    @Autowired
    Authrequest authrequest;
    @Autowired
    Pinwalletrepository pinwalletrepository;

    @Autowired
    Pinwalletutils pinwalletutils;

    @Override
    public WalletTransferRes doAccountTransfer(AccountTransferMerReq dto, String merchantid, PgDetails pg)
            throws JsonProcessingException {

        Pinwalledpayouttrnxs pinwalledpayouttrnxs = new Pinwalledpayouttrnxs();

        WalletTransferRes walletTransferRes = new WalletTransferRes();
        walletTransferRes.setPgId(pg.getPgId());
        walletTransferRes.setStatus("PENDING");
        walletTransferRes.setStatusMessage("PENDING");
        String orderId = Utility.shuffle(dto.getInternalOrderid().replaceAll("[^0-9]", ""));
        if (orderId.length() > 15) {
            orderId = Utility.shuffle(orderId.substring(0, 14));
        }
        pinwalledpayouttrnxs.setPgOrderId(orderId);
        Paymentres paymentres = pinwalletutils.doPayment(dto, merchantid, pg, pinwalledpayouttrnxs);

        logger.info("Pinwallet response " + Utility.convertDTO2JsonString(paymentres) + "");

        if (paymentres.getResponseCode() == 400) {
            walletTransferRes.setStatus("FAILURE");
            walletTransferRes.setStatusMessage("FAILED");
            walletTransferRes.setErrorMsg(paymentres.getMessage());
            return walletTransferRes;
        }

        try {
            if (paymentres.getData() != null) {
                logger.info("Payment Res");
                pinwalledpayouttrnxs.setPinwallettransactionId(paymentres.getData().getPinwalletTransactionId());
                walletTransferRes.setReferenceId(paymentres.getData().getApiTransactionId());
                pinwalletrepository.save(pinwalledpayouttrnxs);
                if (paymentres.getData().getStatus().equalsIgnoreCase("SUCCESS")) {
                    if (paymentres.getData().getRrn() != null) {
                        if (paymentres.getData().getRrn().length() > 3) {
                            walletTransferRes.setStatus("SUCCESS");
                        }
                    }
                }
                if (paymentres.getData().getRrn() != null) {
                    pinwalledpayouttrnxs.setUtrId(paymentres.getData().getRrn());
                    walletTransferRes.setUtr(paymentres.getData().getRrn());
                }
                walletTransferRes.setStatusMessage(paymentres.getData().getMessage());
                // pinwalledpayouttrnxs.setPgOrderId(paymentres.getData().getApiTransactionId());
                pinwalledpayouttrnxs.setPgTransId(paymentres.getData().getPinwalletTransactionId());
                pinwalletrepository.save(pinwalledpayouttrnxs);

            } else {
                // walletTransferRes.setReferenceId(paymentResDto.getData().getReferenceId());
                if (paymentres.getSuccess().equalsIgnoreCase("false")) {
                    walletTransferRes.setStatus("PENDING");
                } else if (paymentres.getSuccess().equalsIgnoreCase("true")
                        && paymentres.getData().getStatus().equalsIgnoreCase("FAILURE")) {
                    walletTransferRes.setStatus("FAILURE");

                }
                walletTransferRes.setErrorMsg(paymentres.getMessage() + "|" + paymentres.getData().getMessage());
                walletTransferRes.setStatusMessage("Trasaction Initiated");
                // walletTransferRes.
            }
            // walletTransferRes.setStatus("PENDING");
            if (walletTransferRes.getErrorMsg() != null) {
                walletTransferRes.setErrorMsg(walletTransferRes.getErrorMsg() + "|" + paymentres.getMessage());
            } else {
                walletTransferRes.setErrorMsg(paymentres.getMessage());
            }
            // walletTransferRes.setStatusMessage("PENDING");
            pinwalletrepository.save(pinwalledpayouttrnxs);
        } catch (Exception e) {
            logger.error("Pinwallet Exceptions ERROR::" + e.getMessage());
            walletTransferRes.setErrorMsg(authrequest.convertDTO2JsonString(e.getMessage()));
        }

        return walletTransferRes;
    }

    @Override
    public TransactionResponseMerRes doWalletTransferStatus(TransferStatusReq dto, String merchantid,
            TransactionDetails transactionDetails, PgDetails pg) throws JsonProcessingException {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method
        // 'doWalletTransferStatus'");
       
        TransactionResponseMerRes transactionResponseMerRes = new TransactionResponseMerRes();
       

        List<String> errorList = new ArrayList<String>();
        errorList.add("Please provide a valid Bank IFSC code.");
        errorList.add("VBA payout is not enabled");
        errorList.add("Service Is Down Kindly Try After Som Time");
        errorList.add("RejectedFailed at beneficiary bank LogRes");
        errorList.add("Invalid Account Number");
        errorList.add("Amount limit exceeded LogRes");
        errorList.add("Service Provider Error");
        errorList.add("Account blockedfrozen LogRes");
        errorList.add("The payee.bankIfsc format is invalid.");
        errorList.add("Request Parameters are Invalid or Incomplete");
        errorList.add("Service Provider DownTime");
        errorList.add("Invalid Beneficiary Mobil NumberMAS");
        errorList.add("Invalid Account");
        errorList.add("Ifsc should be of 11 characters and 5th character should be 0");
        errorList.add("INVALID_ACCOUNT_FAIL");
        errorList.add("IMPS_MODE_FAIL");
        errorList.add("INVALID_IFSC_FAIL");

        logger.info("dowalletStatus is called");
        // Auth requwest
        Authreq authreq = new Authreq();
        StatusReqDto statusReq = new StatusReqDto();

        transactionResponseMerRes.setOrderid(transactionDetails.getInternalOrderId());
        transactionResponseMerRes.setStatus(transactionDetails.getTransactionStatus());
        Pinwalledpayouttrnxs pinwalledpayouttrnxs = pinwalletrepository
                .findByOrderId(transactionDetails.getInternalOrderId());

        if (pinwalledpayouttrnxs == null) {
            logger.info("NO record found in pimwallet model::" + transactionDetails.getInternalOrderId());
            return transactionResponseMerRes;
        }
        if (!transactionDetails.getTransactionStatus().equals("PENDING")) {
            logger.info("Status Unchanged::" + transactionDetails.getInternalOrderId());
            transactionResponseMerRes.setStatusMessage(transactionDetails.getTransactionMessage());
            transactionResponseMerRes.setMessage(transactionDetails.getTransactionMessage());
            return transactionResponseMerRes;
        }
        // if ((pinwalledpayouttrnxs.getPinwallettransactionId() == null)) {
        // logger.info("pimwallet model transactionid is null::" +
        // transactionDetails.getInternalOrderId());
        // return transactionResponseMerRes;
        // }
        authreq.setUserName(pg.getPgConfigKey());
        authreq.setPassword(pg.getPgConfigSecret());
        Authres authres = authrequest.getToken(authreq);

        // get data from the model

        statusReq.setTransactionId(pinwalledpayouttrnxs.getPgOrderId());
        logger.info("Pinwallet status Request" + Utility.convertDTO2JsonString(statusReq));
        pinwalledpayouttrnxs.setStatustRequest(Utility.convertDTO2JsonString(statusReq));
        pinwalletrepository.save(pinwalledpayouttrnxs);
        Paymentres statusRes = authrequest.statusRequest(statusReq, authres.getData().getToken());

        pinwalledpayouttrnxs.setStatusResponse(Utility.convertDTO2JsonString(statusRes));
        pinwalletrepository.save(pinwalledpayouttrnxs);
        if (statusRes.getResponseCode() == 400) {
            if (statusRes.getMessage().equalsIgnoreCase("Transaction Not Found")) {
                transactionResponseMerRes.setStatus("PENDING");
                transactionResponseMerRes.setStatusMessage("Transaction Updated");
                transactionResponseMerRes.setMessage("Transaction from PG");
            }
            logger.info("Status Response::" + Utility.convertDTO2JsonString(statusRes));
            logger.info("400 Status Response");
            return transactionResponseMerRes;
        }
        if (statusRes.getData() == null) {
            logger.info("Status Response::" + Utility.convertDTO2JsonString(statusRes));
            logger.info("Unknown Status Response");
            return transactionResponseMerRes;
        }
        logger.info("Pinwallet status response" + Utility.convertDTO2JsonString(statusRes));
        transactionResponseMerRes.setMessage(statusRes.getData().getMessage());
        transactionResponseMerRes.setStatusMessage(statusRes.getData().getMessage());
        transactionResponseMerRes.setStatus("PENDING");

        if (errorList.contains(statusRes.getData().getMessage())) {
            if (transactionDetails.getTransactionStatus().equals("PENDING")) {
                logger.debug("Listed Failure Errors");
                transactionResponseMerRes.setMessage(statusRes.getData().getMessage());
                transactionResponseMerRes.setStatusMessage(statusRes.getData().getMessage());
                transactionResponseMerRes.setStatus("PENDING"); // Change to Failure when reco complete
            } 
        }
        pinwalledpayouttrnxs.setStatusMessage(statusRes.getData().getStatus());
        pinwalletrepository.save(pinwalledpayouttrnxs);

        if (statusRes.getData().getStatus().equals("SUCCESS")) {
            transactionResponseMerRes.setStatus("SUCCESS");
            if (statusRes.getData().getRrn().length() > 5) {
                logger.info("Made transaction success");
                transactionResponseMerRes.setUtrId(statusRes.getData().getRrn());
            }
        }

        return transactionResponseMerRes;

    }

    @Override
    public WalletTransferRes doAccountTransferUPI(AccountTransferUPIMerReq dto, String merchantid, PgDetails pg) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'doAccountTransferUPI'");
    }

}
