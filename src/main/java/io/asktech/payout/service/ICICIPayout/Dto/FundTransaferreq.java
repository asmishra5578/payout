package io.asktech.payout.service.ICICIPayout.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@JsonIgnoreProperties(ignoreUnknown = true)

public class FundTransaferreq {
    public String walletId;
    public String amount;
    public String beneficiaryIFSC;
    public String beneficiaryAccount;
    public String beneficiaryName;
    public String remarks;
    public String paymentMode;
    public String beneficiaryAddress;
    public String upiAddress;
    public String debitAccount;
    public boolean isCIbPayment;
}
