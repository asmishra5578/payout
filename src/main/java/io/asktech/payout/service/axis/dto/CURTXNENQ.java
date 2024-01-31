package io.asktech.payout.service.axis.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CURTXNENQ {
    public String transaction_id;
    public Object chequeNo;
    public String statusDescription;
    public String batchNo;
    public String utrNo;
    public String transactionStatus;
    public String processingDate;
    public String corpCode;
    public String crn;
    public String responseCode;
    public String paymentMode;
    public String vendorCode;
    public String amount;
    public String corporateAccountNumber;
    public String debitCreditIndicator;
    public String beneficiaryAccountNumber;
    public String extra1;
    public String extra2;
    public String extra3;
    public String extra4;
    public String extra5;
}
