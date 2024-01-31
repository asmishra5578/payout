package io.asktech.payout.service.kitzone.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdditionalDetails {
    @JsonProperty("ACCOUNT NUMBER") 
    private String aCCOUNTNUMBER;
    @JsonProperty("IFSC CODE") 
    private String iFSCCODE;
    @JsonProperty("BANK RRN") 
    private String bANKRRN;
    @JsonProperty("UTR Number") 
    public String utrNumber;
    @JsonProperty("ERROR MESSAGE")
    private String errorMessage;
}
