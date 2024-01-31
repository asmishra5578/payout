package io.asktech.payout.dto.merchant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionResponseMerRes {
	private String utrId;
	private String orderid;
	private String status;
	private String message;
	private String amount;
	private String statusMessage;
	private String errorMsg;
	
}
