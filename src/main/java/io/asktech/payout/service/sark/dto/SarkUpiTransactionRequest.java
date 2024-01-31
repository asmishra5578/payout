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
public class SarkUpiTransactionRequest {
    @JsonProperty("user_id")
    private String user_id;
    @JsonProperty("api_token")
    private String api_token;
    @JsonProperty("key")
    private String key;
    @JsonProperty("senderMobile")
    private String senderMobile;
    @JsonProperty("senderName")
    private String senderName;
    @JsonProperty("beneName")
    private String beneName;
    @JsonProperty("upiId")
    private String upiId;
    @JsonProperty("txnAmount")
    private String txnAmount;
    @JsonProperty("end_point_ip")
    private String end_point_ip;
    @JsonProperty("latitude")
    private String latitude;
    @JsonProperty("longitude")
    private String longitude;
    @JsonProperty("uniqueRefId")
    private String uniqueRefId;
}
