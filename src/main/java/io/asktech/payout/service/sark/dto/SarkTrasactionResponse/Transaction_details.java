package io.asktech.payout.service.sark.dto.SarkTrasactionResponse;
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
public class Transaction_details {
    @JsonProperty("txnTime")
    private String txnTime;
    @JsonProperty("uniqueRefId")
    private String uniqueRefId;
    @JsonProperty("txnId")
    private String txnId;
    @JsonProperty("bankRef")
    private String bankRef;
}
