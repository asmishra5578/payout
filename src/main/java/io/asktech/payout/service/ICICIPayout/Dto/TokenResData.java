package io.asktech.payout.service.ICICIPayout.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)

public class TokenResData{
    public String name;
    public String mobile;
    public String emailId;
    public String isActive;
    public String walletId;
    public String role;
    public Object password;
    public String aesKey;
    public String token;
}
