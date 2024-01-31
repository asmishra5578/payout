package io.asktech.payout.service.kitzone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EncryptionPaymentDto {
    
    @JsonProperty("UserId") 
    private String userId;
    @JsonProperty("Amount") 
    private String amount;
    @JsonProperty("AccountNumber") 
    private String accountNumber;
    @JsonProperty("IFSCCode") 
    private String iFSCCode;
    @JsonProperty("Mode") 
    private String mode;
    @JsonProperty("Remarks") 
    private String remarks;
    @JsonProperty("OTP") 
    private String oTP;
    @JsonProperty("PIN") 
    private String pIN;
    private String orderID;


}
