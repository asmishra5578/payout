package io.asktech.payout.service.sark.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SarkTransactionResponse {
    @JsonProperty("txnTime")
    private String txnTime;
    @JsonProperty("message")
    private String message;
    @JsonProperty("status")
    private String status;
    @JsonProperty("txnId")
    private String txnId;
    @JsonProperty("bankRef")
    private String bankRef;

}
