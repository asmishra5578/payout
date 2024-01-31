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
public class SarkStatusResponse {
    @JsonProperty("number")
    private String number;
    @JsonProperty("statusDescription")
    private String statusDescription;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("txnTime")
    private String txnTime;
    @JsonProperty("refId")
    private String refId;
    @JsonProperty("message")
    private String message;
    @JsonProperty("status")
    private String status;
    @JsonProperty("txnId")
    private String txnId;
}
