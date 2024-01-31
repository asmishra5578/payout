package io.asktech.payout.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TransactionReversalResponse {
    private String orderId;
    private String status;
    private String message;
}
