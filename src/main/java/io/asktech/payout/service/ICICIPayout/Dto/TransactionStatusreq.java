package io.asktech.payout.service.ICICIPayout.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class TransactionStatusreq {
    String orderId;
    String walletId;
}

