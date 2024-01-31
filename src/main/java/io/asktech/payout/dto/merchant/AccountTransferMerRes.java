package io.asktech.payout.dto.merchant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountTransferMerRes {
	private String orderid;
	private String status;
	private String message;
	
}
