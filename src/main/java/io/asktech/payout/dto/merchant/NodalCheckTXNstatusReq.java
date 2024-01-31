package io.asktech.payout.dto.merchant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter


public class NodalCheckTXNstatusReq {

	private String channel;
	private String transactionType;
	private String txnReqId;
	
}
