package io.asktech.payout.service.kitzone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayoutRes{

    @JsonProperty("Code") 
    public int code;
    @JsonProperty("Status") 
    public String status;
    @JsonProperty("TransactionId") 
    public String transactionId;
    @JsonProperty("OrderId") 
    public String orderId;
    @JsonProperty("Message") 
    public String message;
    @JsonProperty("ResultDt") 
    public ResultDtIn resultDt;

}