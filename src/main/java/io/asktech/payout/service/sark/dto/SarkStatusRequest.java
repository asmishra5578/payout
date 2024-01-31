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
public class SarkStatusRequest {
    @JsonProperty("user_id")
    private String user_id;
    @JsonProperty("api_token")
    private String api_token;
    @JsonProperty("key")
    private String key;
    @JsonProperty("uniqueRefId")
    private String uniqueRefId;
    @JsonProperty("txnDate")
    private String txnDate;
}
