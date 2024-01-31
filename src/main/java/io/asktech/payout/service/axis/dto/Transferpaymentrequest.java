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

public class Transferpaymentrequest {
    @JsonProperty("PaymentDetail") 
    public PaymentDetail paymentDetail;
    // @JsonProperty("BeneficiaryRegistrationRequestBody") 
    // public BeneficiaryRegistrationRequestBody beneficiaryRegistrationRequestBody;
        @JsonProperty("TransferPaymentRequestBodyEncrypted") 
    public String transferPaymentRequestBodyEncrypted;


    
}
