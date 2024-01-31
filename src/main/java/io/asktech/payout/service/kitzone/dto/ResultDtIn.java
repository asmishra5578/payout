package io.asktech.payout.service.kitzone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultDtIn{

    // @JsonProperty("Code") 
    // public int code;
    // @JsonProperty("Status") 
    // public String status;
    // @JsonProperty("TransactionId") 
    // public String transactionId;
    // @JsonProperty("Message") 
    // public String message;
    // @JsonProperty("ResultDt") 
    // public ResultDtRes resultDt;
    public String status;
    public String subCode;
    public String message;
    public DataRes data;
}