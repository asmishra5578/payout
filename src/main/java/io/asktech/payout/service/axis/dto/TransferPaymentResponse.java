package io.asktech.payout.service.axis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
// import com.fasterxml.jackson.annotation.JsonProperty;

// import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
// import com.fasterxml.jackson.annotation.JsonInclude;
// import com.fasterxml.jackson.annotation.JsonProperty;
// import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferPaymentResponse {
    @JsonProperty("SubHeader")
    public SubHeader SubHeader;
    @JsonProperty("TransferPaymentResponseBodyEncrypted")
    public String TransferPaymentResponseBodyEncrypted;
   
}
