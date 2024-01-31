package io.asktech.payout.dto.nodal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class MerNodalCheckTXNstatusReq {

	private String merchantId;
	private String channel;
	private String transactionType;
	private String txnReqId;
	
}
