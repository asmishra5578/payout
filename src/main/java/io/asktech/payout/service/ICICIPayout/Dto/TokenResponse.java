package io.asktech.payout.service.ICICIPayout.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)

public class TokenResponse 
    {
    public String success;
    public TokenResData data;
    public String message;
    public String errors;
    public String exception;
}

