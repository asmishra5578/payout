package io.asktech.payout.service.kitzone.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transfer{
    public int referenceId;
    public String bankAccount;
    public String ifsc;
    public String beneId;
    public String amount;
    public String status;
    public String addedOn;
    public String processedOn;
    public String transferMode;
    public int acknowledged;
}

