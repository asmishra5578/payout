package io.asktech.payout.dto.nodal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PayTmNodalBalTransReq {

	private String amount;
	private String benAccNo;
	private String benIfsc;
	private String benName;
	private String channel;
	private String transactionType;
	private String txnReqId;
	private String remarks;

}
