package io.asktech.payout.service.safex.dto;

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
public class Transaction { 
   @JsonProperty("requestType")
   private String requestType;
   @JsonProperty("requestSubType")
   private String requestSubType;
   @JsonProperty("channel")
   private String channel;
   @JsonProperty("tranCode")
   private String tranCode;
   @JsonProperty("txnAmt")
   private String txnAmt;
}
