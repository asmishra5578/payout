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
public class SafexRequestDto {
   @JsonProperty("payOutBean")
   private PayOutBean payOutBean;
   @JsonProperty("header")
   private Header header;
   @JsonProperty("transaction")
   private Transaction transaction;
}
