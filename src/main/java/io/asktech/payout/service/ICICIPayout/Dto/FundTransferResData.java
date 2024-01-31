package io.asktech.payout.service.ICICIPayout.Dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FundTransferResData 
    {
    public String orderId;
    public String status;
    public String transactionId;
    public Date creationDateTime;
}

