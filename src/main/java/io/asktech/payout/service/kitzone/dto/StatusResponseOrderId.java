package io.asktech.payout.service.kitzone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusResponseOrderId {
    @JsonProperty("Code") 
    public int code;
    @JsonProperty("Status") 
    public String status;
    @JsonProperty("Message") 
    public String message;
    @JsonProperty("ResultDt") 
    public ResultOrderId resultDt;
}