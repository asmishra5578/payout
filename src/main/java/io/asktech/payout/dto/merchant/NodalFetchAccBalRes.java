package io.asktech.payout.dto.merchant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class NodalFetchAccBalRes {
private String accountNumber;
private String effectiveBalance;
private String slfdBalance;
private String status;
private String txn_id;
}