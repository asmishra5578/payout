package io.asktech.payout.service.ICICIPayout.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)

public class FundTransferres {
    public String success;
    public FundTransferResData data;
    public String message;
    public Object errors;
    public Object exception;
}


