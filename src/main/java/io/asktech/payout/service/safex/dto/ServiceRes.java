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
public class ServiceRes {
   @JsonProperty("uId")
   private String uId;
   @JsonProperty("payload")
   private String payload;
   @JsonProperty("mId")
   private String mId;
   @JsonProperty("agId")
   private String agId;
   @JsonProperty("requestId")
   private String requestId;
   @JsonProperty("respTime")
   private String respTime;
   @JsonProperty("reqTime")
   private String reqTime;
}
