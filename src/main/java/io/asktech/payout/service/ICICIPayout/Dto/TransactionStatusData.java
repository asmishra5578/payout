package io.asktech.payout.service.ICICIPayout.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)

public class TransactionStatusData {
    public String orderId;
    public String transactionId;
    public String utr;
    public String status;
    public String creationDateTime;
    public Object statusUpdateDateTime;
    public String amount;
    public String commision;
    public String tax;
    public String currency;
    public String accountNumber;
    public String ifsc;
    public String beneficiary;
    public String mode;
    public Object vpa;
}
