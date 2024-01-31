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
public class SarkTrasactionRequest {
    @JsonProperty("user_id")
    private String user_id;
    @JsonProperty("api_token")
    private String api_token;
    @JsonProperty("key")
    private String key;
    @JsonProperty("beneName")
    private String beneName;
    @JsonProperty("txnMode")
    private String txnMode;
    @JsonProperty("mobile")
    private String mobile;
    @JsonProperty("senderFirstName")
    private String senderFirstName;
    @JsonProperty("bankName")
    private String bankName;
    @JsonProperty("accountNumber")
    private String accountNumber;
    @JsonProperty("ifsc")
    private String ifsc;
    @JsonProperty("uniqueRefId")
    private String uniqueRefId;
    @JsonProperty("senderLastName")
    private String senderLastName;
    @JsonProperty("txnAmount")
    private String txnAmount;
    @JsonProperty("end_point_ip")
    private String end_point_ip;
    @JsonProperty("latitude")
    private String latitude;
    @JsonProperty("longitude")
    private String longitude;
}
