package io.asktech.payout.dto.merchant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class NodalCheckTXNstatusRes {
private String status;
private String amount;
private String transactionStatus;
private String response_code;
private String txn_id;
}