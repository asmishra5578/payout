package io.asktech.payout.service.kitzone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankResponseDto {
    @JsonProperty("MESSAGE") 
    public String mESSAGE;
    @JsonProperty("ERRORCODE") 
    public String eRRORCODE;
    @JsonProperty("RESPONSECODE") 
    public String rESPONSECODE;
    @JsonProperty("STATUS") 
    public String sTATUS;
    @JsonProperty("RESPONSE") 
    public String rESPONSE;
}
