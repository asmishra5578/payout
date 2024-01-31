package io.asktech.payout.dto.nodal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PayTmNodalCheckTXNstatusReq {

	private String channel;
	private String transactionType;
	private String txnReqId;
}
