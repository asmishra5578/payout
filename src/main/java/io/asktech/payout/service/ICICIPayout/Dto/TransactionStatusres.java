package io.asktech.payout.service.ICICIPayout.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)

public class TransactionStatusres {
    public boolean success;
    public TransactionStatusData data;
    public String message;
    public String errors;
    public String exception;
}