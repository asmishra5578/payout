package io.asktech.payout.service.axis.dto;
import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferPaymentRequest {
    @JsonProperty("SubHeader") 
    public SubHeader subHeader;
    // @JsonProperty("BeneficiaryRegistrationRequestBody") 
    // public BeneficiaryRegistrationRequestBody beneficiaryRegistrationRequestBody;
    @JsonProperty("TransferPaymentRequestBodyEncrypted") 
    public String transferPaymentRequestBodyEncrypted;  
}
