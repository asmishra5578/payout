package io.asktech.payout.service.kitzone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultDtDto {
    @JsonProperty("BankResponse") 
    public BankResponseDto bankResponse;
    @JsonProperty("TransactionID") 
    public String transactionID;
    @JsonProperty("Amount") 
    public String amount;
    @JsonProperty("AccountNumber") 
    public String accountNumber;
    @JsonProperty("IfscCode") 
    public String ifscCode;
    @JsonProperty("Mode") 
    public String mode;
    @JsonProperty("FireDate") 
    public String fireDate;
    @JsonProperty("OrderId") 
    public String orderId;
}
