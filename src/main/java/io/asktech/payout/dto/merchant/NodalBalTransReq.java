package io.asktech.payout.dto.merchant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter


public class NodalBalTransReq {

	private String amount;
	private String benAccNo;
	private String benIfsc;
	private String benName;
	private String channel;
	private String transactionType;
	private String txnReqId;
	private String remarks;
	
}
