package io.asktech.payout.service.payg;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.asktech.payout.dto.merchant.AccountTransferMerReq;
import io.asktech.payout.dto.merchant.TransactionResponseMerRes;
import io.asktech.payout.dto.reqres.TransferStatusReq;
import io.asktech.payout.dto.reqres.WalletTransferRes;
import io.asktech.payout.modal.merchant.PgDetails;
import io.asktech.payout.modal.merchant.TransactionDetails;
import io.asktech.payout.service.payg.dto.AddContact;
import io.asktech.payout.service.payg.dto.AddContactResponse;
import io.asktech.payout.service.payg.dto.FundTransferDto;
import io.asktech.payout.service.payg.dto.FundTransferResponseDto;
import io.asktech.payout.service.payg.dto.TransactionStatusDto;
import io.asktech.payout.service.payg.dto.TransactionStatusResponseDto;
import io.asktech.payout.service.payg.dto.webhook.PayGwebhook;
import io.asktech.payout.service.payg.modals.PaygResponseDetails;
import io.asktech.payout.service.payg.repo.PaygResponseDetailsRepo;
import io.asktech.payout.service.payg.requests.PaygPaymentRequest;
import io.asktech.payout.utils.Utility;

@Service
public class PaygProcessor {
    @Autowired
    SaveDetails saveDetails;
    @Autowired
    PaygPaymentRequest paygPaymentRequest;
    static Logger logger = LoggerFactory.getLogger(PaygProcessor.class);

    public WalletTransferRes doAccountTransfer(AccountTransferMerReq dto, String merchantid, PgDetails pg)
            throws JsonProcessingException {

        WalletTransferRes walletTransferRes = new WalletTransferRes();
        walletTransferRes.setPgId(pg.getPgId());

        AddContact createContact = setContactDetails(dto, merchantid, pg);

        /* Save Contact Details */
        saveDetails.saveContactRequest(createContact, merchantid, "CUST" + dto.getInternalOrderid());
        /* Send Contact Request */
        try {
            AddContactResponse addContactResponse = paygPaymentRequest.addContact(createContact, pg, merchantid);
            createContact.setCustomerId(addContactResponse.getCustomerId());
            createContact.setPayoutCustomerkeyId(addContactResponse.getPayoutCustomerkeyId());
            createContact.setUpdatedDatetime(addContactResponse.getUpdatedDatetime());
            saveDetails.saveContactRequest(createContact, merchantid, "CUST" + dto.getInternalOrderid());
            FundTransferDto fundTransferDto = setFundTransfer(dto, addContactResponse.getPayoutCustomerkeyId(), pg);
            saveDetails.saveFundtransferRequest(fundTransferDto, dto.getInternalOrderid(), merchantid);
            try {
                FundTransferResponseDto fundTransferResponse = paygPaymentRequest.fundTransfer(fundTransferDto, pg,
                        merchantid);
                if (fundTransferResponse != null) {
                    saveDetails.saveFundtransferResponse(fundTransferResponse, dto.getInternalOrderid(), merchantid);
                }
                return walletStatusUpdate(walletTransferRes, fundTransferResponse);
            } catch (Exception e) {
                logger.info("fundTransferResponse::" + e.getMessage());
                walletTransferRes.setStatus("FAILURE");
                walletTransferRes.setStatusMessage("Transfer Api ERROR");
                return walletTransferRes;
            }

        } catch (Exception e) {
            logger.info("AddContactResponse::" + e.getMessage());
            walletTransferRes.setStatus("FAILURE");
            walletTransferRes.setStatusMessage("Contact Api ERROR");
            return walletTransferRes;
        }
        /* Update Save Contact Details */

    }

    @Autowired
    PaygResponseDetailsRepo paygResponseDetailsRepo;

    public TransactionResponseMerRes doWalletTransferStatus(TransferStatusReq dto, String merchantid,
            TransactionDetails transactionDetails, PgDetails pg) throws JsonProcessingException {

        PaygResponseDetails paygRes = paygResponseDetailsRepo
                .findByEazyOrderId(transactionDetails.getInternalOrderId());
        logger.info("PaygResponseDetails::" + Utility.convertDTO2JsonString(paygRes));
        TransactionResponseMerRes transactionResponseMerRes = new TransactionResponseMerRes();
        if (paygRes != null) {
            TransactionStatusDto transactionStatusDto = new TransactionStatusDto();
            transactionStatusDto.setMerchantKeyId(pg.getPgConfig1());
            transactionStatusDto.setTransactionId(paygRes.getTransactionId());
            transactionStatusDto.setPayOutKeyId(paygRes.getPayOutKeyId());
            TransactionStatusResponseDto response = paygPaymentRequest.transactionStatus(transactionStatusDto, pg,
                    merchantid);
            if (response != null) {
                transactionResponseMerRes = walletStatusUpdateByStatusApi(transactionResponseMerRes, response);
            }else{
                transactionResponseMerRes.setStatus(transactionDetails.getTransactionStatus());
                transactionResponseMerRes.setMessage(transactionDetails.getTransactionMessage());
            }
        }else{
            transactionResponseMerRes.setStatus("FAILURE");
            transactionResponseMerRes.setMessage("Transaction Incomplete");
        }
        transactionResponseMerRes.setAmount(transactionDetails.getAmount());
        transactionResponseMerRes.setOrderid(transactionDetails.getOrderId());

        return transactionResponseMerRes;
    }

    private AddContact setContactDetails(AccountTransferMerReq dto, String merchantid, PgDetails pg) {
        AddContact createContact = new AddContact();

        createContact.setEmail("support@analytiqbv.com");
        createContact.setAddress("Pitampura");
        createContact.setRequestUniqueId("CUST" + dto.getInternalOrderid());
        createContact.setCompanyName("Analytiq Business Pvt Ltd");
        createContact.setMerchantKeyId(pg.getPgConfig1());
        createContact.setMobileNo(dto.getPhonenumber());
        createContact.setFirstName(dto.getBeneficiaryName());
        createContact.setZipCode("110034");
        createContact.setCity("New Delhi");
        createContact.setState("Delhi");
        createContact.setCountry("India");
        createContact.setCustomerId("CUST" + dto.getInternalOrderid());
        // createContact.setCustomerId("");
        createContact.setLastName("");
        createContact.setMobileNo2("");
        createContact.setEmail2("");
        createContact.setUserDefined1("");
        createContact.setUserDefined4("");
        createContact.setUserDefined7("");
        createContact.setUserDefined8("");
        createContact.setUserDefined9("");
        createContact.setUserDefined6("");
        createContact.setUserDefined10("");
        createContact.setUserDefined5("");
        createContact.setUserDefined2("");
        createContact.setUserDefined3("");
        createContact.setPayoutCustomerkeyId("");
        createContact.setUpdatedDatetime("");
        return createContact;
    }

    private FundTransferDto setFundTransfer(AccountTransferMerReq dto, String payoutCustomerkeyId, PgDetails pg) {
        FundTransferDto fundTransfer = new FundTransferDto();
        fundTransfer.setBankName("Idbi");
        fundTransfer.setProductData(
                "{'PaymentReason':'OnlineOrder for OrderNo- 1234', 'ItemId':'T-shirt', 'Size':'medium', 'AppName':'yourAppName'}");
        fundTransfer.setBeneficiaryVerification("0");
        fundTransfer.setUniqueRequestId(dto.getInternalOrderid());
        fundTransfer.setBankCountry("");
        fundTransfer.setAmount(dto.getAmount());
        fundTransfer.setPayoutCustomerkeyId(payoutCustomerkeyId);
        fundTransfer.setBeneficiaryName(dto.getBeneficiaryName());
        fundTransfer.setBankCode(dto.getIfsc());
        fundTransfer.setPayOutType("Immediate");
        fundTransfer.setAccountNumber(dto.getBankaccount());
        fundTransfer.setPayOutDate("");
        fundTransfer.setPaymentType(dto.getRequestType());
        fundTransfer.setBranchName("Hyderabad");
        fundTransfer.setMerchantKeyId(pg.getPgConfig1());
        fundTransfer.setKycData("");

        return fundTransfer;
    }

    public void payGwebhookProcess(PayGwebhook payGwebhook){
        
    }
    private WalletTransferRes walletStatusUpdate(WalletTransferRes walletTransferRes, FundTransferResponseDto res) {
        if (res.getResponseCode().equals("1")) {
            walletTransferRes.setStatus("ACCEPTED");
            walletTransferRes.setStatusMessage(res.getResponseText());
            walletTransferRes.setReferenceId(res.getTransactionId());
            // walletTransferRes.setUtr(res.getData().getUtr());
        } else if (res.getResponseCode().equals("2")) {
            walletTransferRes.setStatus("FAILURE");
            walletTransferRes.setStatusMessage(res.getResponseText());
            walletTransferRes.setReferenceId(res.getTransactionId());
            // walletTransferRes.setUtr(res.getData().getUtr());
        } else if (res.getResponseCode().equals("4") || res.getResponseCode().equals("5")) {
            walletTransferRes.setStatus("PENDING");
            walletTransferRes.setStatusMessage(res.getResponseText());
            walletTransferRes.setReferenceId(res.getTransactionId());
        }
        return walletTransferRes;
    }

    private TransactionResponseMerRes walletStatusUpdateByStatusApi(TransactionResponseMerRes walletTransferRes,
            TransactionStatusResponseDto res) {
        if (res.getStatus().equals("0") || res.getStatus().equals("1")) {
            walletTransferRes.setStatus("PENDING");
            walletTransferRes.setStatusMessage(res.getResponseText());
            if (res.getUTRNo() != null) {
                walletTransferRes.setUtrId(res.getUTRNo());
            }
        } else if (res.getStatus().equals("2") || res.getStatus().equals("4")) {
            walletTransferRes.setStatus("FAILURE");
            walletTransferRes.setStatusMessage(res.getResponseText());
            if (res.getUTRNo() != null) {
                walletTransferRes.setUtrId(res.getUTRNo());
            }
        } else if (res.getStatus().equals("3")) {
            walletTransferRes.setStatus("SUCCESS");
            walletTransferRes.setStatusMessage(res.getResponseText());
            walletTransferRes.setUtrId(res.getUTRNo());
        }
        return walletTransferRes;
    }
}
