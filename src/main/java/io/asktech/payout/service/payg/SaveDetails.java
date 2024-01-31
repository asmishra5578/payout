package io.asktech.payout.service.payg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import io.asktech.payout.service.payg.dto.AddContact;
import io.asktech.payout.service.payg.dto.FundTransferDto;
import io.asktech.payout.service.payg.dto.FundTransferResponseDto;
import io.asktech.payout.service.payg.modals.PaygContact;
import io.asktech.payout.service.payg.modals.PaygRequestDetails;
import io.asktech.payout.service.payg.modals.PaygResponseDetails;
import io.asktech.payout.service.payg.repo.PaygContactRepo;
import io.asktech.payout.service.payg.repo.PaygRequestDetailsRepo;
import io.asktech.payout.service.payg.repo.PaygResponseDetailsRepo;

@Component
public class SaveDetails {
    @Autowired
    PaygContactRepo paygContactRepo;

    @Autowired
    PaygRequestDetailsRepo paygRequestDetailsRepo;

    @Autowired
    PaygResponseDetailsRepo paygResponseDetailsRepo;

    @Async
    public void saveFundtransferRequest(FundTransferDto fundTransfer, String orderId, String merchantId) {
        PaygRequestDetails details = paygRequestDetailsRepo.findByEazyOrderId(orderId);
        if (details == null) {
            details = new PaygRequestDetails();
        }
        details.setBankName(fundTransfer.getBankName());
        details.setProductData(fundTransfer.getProductData());
        details.setBeneficiaryVerification(fundTransfer.getBeneficiaryVerification());
        details.setUniqueRequestId(fundTransfer.getUniqueRequestId());
        details.setBankCountry(fundTransfer.getBankCountry());
        details.setAmount(fundTransfer.getAmount());
        details.setPayoutCustomerkeyId(fundTransfer.getPayoutCustomerkeyId());
        details.setBeneficiaryName(fundTransfer.getBeneficiaryName());
        details.setBankCode(fundTransfer.getBankCode());
        details.setPayOutType(fundTransfer.getPayOutType());
        details.setAccountNumber(fundTransfer.getAccountNumber());
        details.setPayOutDate(fundTransfer.getPayOutDate());
        details.setPaymentType(fundTransfer.getPaymentType());
        details.setBranchName(fundTransfer.getBranchName());
        details.setMerchantKeyId(fundTransfer.getMerchantKeyId());
        details.setKycData(fundTransfer.getKycData());
        details.setEazyMerchantId(merchantId);
        details.setEazyOrderId(orderId);
        paygRequestDetailsRepo.save(details);

    }

    @Async
    public void saveFundtransferResponse(FundTransferResponseDto fundTransfer, String orderId, String merchantId) {
        PaygResponseDetails details = paygResponseDetailsRepo.findByEazyOrderId(orderId);
        if (details == null) {
            details = new PaygResponseDetails();
        }
        details.setStatus(fundTransfer.getStatus());
        details.setTotalFeeAmount(fundTransfer.getTotalFeeAmount());
        details.setBuyAmount(fundTransfer.getBuyAmount());
        details.setTransactionType(fundTransfer.getTransactionType());
        details.setResponseCode(fundTransfer.getResponseCode());
        details.setUniqueRequestId(fundTransfer.getUniqueRequestId());
        details.setAmount(fundTransfer.getAmount());
        details.setPayoutCustomerkeyId(fundTransfer.getPayoutCustomerkeyId());
        details.setGSTAmount(fundTransfer.getGSTAmount());
        details.setBatchId(fundTransfer.getBatchId());
        details.setPayOutKeyId(fundTransfer.getPayOutKeyId());
        details.setPayOutBeneficiaryKeyId(fundTransfer.getPayOutBeneficiaryKeyId());
        details.setPayOutType(fundTransfer.getPayOutType());
        details.setFeeCode(fundTransfer.getFeeCode());
        details.setTransactionId(fundTransfer.getTransactionId());
        details.setPayOutDate(fundTransfer.getPayOutDate());
        details.setAdditionalFee(fundTransfer.getAdditionalFee());
        details.setBuyGSTAmount(fundTransfer.getBuyGSTAmount());
        details.setPaymentType(fundTransfer.getPaymentType());
        details.setResponseText(fundTransfer.getResponseText());
        details.setUpdatedDateTime(fundTransfer.getUpdatedDateTime());
        details.setGSTPercentage(fundTransfer.getGSTPercentage());
        details.setMerchantKeyId(fundTransfer.getMerchantKeyId());
        details.setFeeAmount(fundTransfer.getFeeAmount());
        details.setEazyMerchantId(merchantId);
        details.setEazyOrderId(orderId);
        paygResponseDetailsRepo.save(details);

    }

    @Async
    public void saveContactRequest(AddContact addContact, String orderId, String merchantId) {
        PaygContact details = paygContactRepo.findByEazyPayOrderId(orderId);
        if (details == null) {
            details = new PaygContact();
        }

        details.setUserDefined8(addContact.getUserDefined8());
        details.setUserDefined9(addContact.getUserDefined9());
        details.setUserDefined6(addContact.getUserDefined6());
        details.setEmail(addContact.getEmail());
        details.setUserDefined7(addContact.getUserDefined7());
        details.setAddress(addContact.getAddress());
        details.setMobileNo2(addContact.getMobileNo2());
        details.setUserDefined1(addContact.getUserDefined1());
        details.setUserDefined4(addContact.getUserDefined4());
        details.setRequestUniqueId(addContact.getRequestUniqueId());
        details.setUserDefined5(addContact.getUserDefined5());
        details.setUserDefined2(addContact.getUserDefined2());
        details.setUserDefined3(addContact.getUserDefined3());
        details.setCompanyName(addContact.getCompanyName());
        details.setEmail2(addContact.getEmail2());
        details.setUserDefined10(addContact.getUserDefined10());
        details.setMerchantKeyId(addContact.getMerchantKeyId());
        details.setMobileNo(addContact.getMobileNo());
        details.setFirstName(addContact.getFirstName());
        details.setZipCode(addContact.getZipCode());
        details.setCustomerId(addContact.getCustomerId());
        details.setCity(addContact.getCity());
        details.setState(addContact.getState());
        details.setCountry(addContact.getCountry());
        details.setLastName(addContact.getLastName());
        paygContactRepo.save(details);

    }
}