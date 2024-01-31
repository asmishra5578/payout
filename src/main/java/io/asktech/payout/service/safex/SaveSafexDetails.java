package io.asktech.payout.service.safex;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import io.asktech.payout.service.safex.dto.PayOutBean;
import io.asktech.payout.service.safex.models.SafexPayloadBean;
import io.asktech.payout.service.safex.repo.SafexPayloadBeanRepo;
import io.asktech.payout.service.safex.repo.SefexRequestRepo;
import io.asktech.payout.service.safex.models.SafexRequests;

@Component
public class SaveSafexDetails {
    @Autowired
    SafexPayloadBeanRepo safexPayloadBeanRepo;
    @Autowired
    SefexRequestRepo sefexRequestRepo;

    @Async
    public void savePayloadBean(PayOutBean payOutBean) {
        SafexPayloadBean safexPayloadBean = null;
        safexPayloadBean = safexPayloadBeanRepo.findByOrderRefNo(payOutBean.getOrderRefNo());

        if (safexPayloadBean == null) {
            safexPayloadBean = new SafexPayloadBean();
            safexPayloadBean.setAccountHolderName(payOutBean.getAccountHolderName());
            safexPayloadBean.setAccountNo(payOutBean.getAccountNo());
            safexPayloadBean.setAccountType(payOutBean.getAccountType());
            safexPayloadBean.setBankName(payOutBean.getBankName());
            safexPayloadBean.setIfscCode(payOutBean.getIfscCode());
            safexPayloadBean.setMobileNo(payOutBean.getMobileNo());
            safexPayloadBean.setTxnAmount(payOutBean.getTxnAmount());
            safexPayloadBean.setTxnType(payOutBean.getTxnType());
            safexPayloadBean.setOrderRefNo(payOutBean.getOrderRefNo());
        } else {
            safexPayloadBean.setAccountHolderName(payOutBean.getAccountHolderName());
            safexPayloadBean.setPayoutDate(payOutBean.getPayoutDate());
            safexPayloadBean.setOrderRefNo(payOutBean.getOrderRefNo());
            safexPayloadBean.setPayoutTime(payOutBean.getPayoutTime());
            safexPayloadBean.setTxnStatus(payOutBean.getTxnStatus());
            safexPayloadBean.setAccountNo(payOutBean.getAccountNo());
            safexPayloadBean.setCustomerId(payOutBean.getCustomerId());
            safexPayloadBean.setTxn_InitiateDate(payOutBean.getTxn_InitiateDate());
            safexPayloadBean.setMerid(payOutBean.getId());
            safexPayloadBean.setIfscCode(payOutBean.getIfscCode());
            safexPayloadBean.setBankTransactionRefNo(payOutBean.getBankTransactionRefNo());
            safexPayloadBean.setBankRefNo(payOutBean.getBankRefNo());
            safexPayloadBean.setStatusDesc(payOutBean.getStatusDesc());
            safexPayloadBean.setAccountType(payOutBean.getAccountType());
            safexPayloadBean.setCount(payOutBean.getCount());
            safexPayloadBean.setAggregatorId(payOutBean.getAggregatorId());
            safexPayloadBean.setMobileNo(payOutBean.getMobileNo());
            safexPayloadBean.setPayoutId(payOutBean.getPayoutId());
            safexPayloadBean.setCustomerName(payOutBean.getCustomerName());
            safexPayloadBean.setBankStatus(payOutBean.getBankStatus());
            safexPayloadBean.setAggregtorName(payOutBean.getAggregtorName());
            safexPayloadBean.setBankResponseCode(payOutBean.getBankResponseCode());
            safexPayloadBean.setTxnAmount(payOutBean.getTxnAmount());
            safexPayloadBean.setTxnDate(payOutBean.getTxnDate());
            safexPayloadBean.setStatusCode(payOutBean.getStatusCode());
            safexPayloadBean.setBankName(payOutBean.getBankName());
        }
        safexPayloadBeanRepo.save(safexPayloadBean);
    }

    @Async
    public void saveSafexRequest(String orderId, String reqFundTransferPayload, String resFundTransferPayload,
            String reqSessionApiPayload, String resSessionApiPayload, String reqStatusApiPayload,
            String resStatusApiPayload, String responseFundTransferId, String responseApiSessionId,
            String responseStatusApiSessionId, String sessionKey, String trxStatus) {
        SafexRequests safexRequests = null;
        safexRequests = sefexRequestRepo.findByOrderId(orderId);
        if (safexRequests == null) {
            safexRequests = new SafexRequests();
            safexRequests.setOrderId(orderId);
        }
        if (reqFundTransferPayload != null) {
            safexRequests.setReqFundTransferPayload(reqFundTransferPayload);
        }
        if (resFundTransferPayload != null) {
            safexRequests.setResFundTransferPayload(resFundTransferPayload);
        }
        if (reqSessionApiPayload != null) {
            safexRequests.setReqSessionApiPayload(reqSessionApiPayload);
        }
        if (resSessionApiPayload != null) {
            safexRequests.setResSessionApiPayload(resSessionApiPayload);
        }
        if (reqStatusApiPayload != null) {
            safexRequests.setReqStatusApiPayload(reqStatusApiPayload);
        }
        if (resStatusApiPayload != null) {
            safexRequests.setResStatusApiPayload(resStatusApiPayload);
        }
        if (responseFundTransferId != null) {
            safexRequests.setResponseFundTransferId(responseFundTransferId);
        }
        if (responseApiSessionId != null) {
            safexRequests.setResponseApiSessionId(responseApiSessionId);
        }
        if (responseStatusApiSessionId != null) {
            safexRequests.setResponseStatusApiSessionId(responseStatusApiSessionId);
        }
        if (sessionKey != null) {
            safexRequests.setSessionKey(sessionKey);
        }
        if (trxStatus != null) {
            safexRequests.setTrxStatus(trxStatus);
        }
        sefexRequestRepo.save(safexRequests);
    }
    
    public String getPayoutBean(String orderId){
        SafexPayloadBean  safexPayloadBean = safexPayloadBeanRepo.findByOrderRefNo(orderId);
        if(safexPayloadBean != null){
            return safexPayloadBean.getPayoutId();
        }
        return null;
    }
}
