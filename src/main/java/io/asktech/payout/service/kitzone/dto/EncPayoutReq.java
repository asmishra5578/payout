package io.asktech.payout.service.kitzone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EncPayoutReq {
    public String amount;
    public String transferMode;
    public String transferId;
    public String remarks;
    @JsonProperty("OTP") 
    public String oTP;
    @JsonProperty("PIN") 
    public String pIN;
    @JsonProperty("UserId") 
    public String userId;
    public BankDetailsEncReq beneDetails;
}
