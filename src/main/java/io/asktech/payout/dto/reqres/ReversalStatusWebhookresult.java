package io.asktech.payout.dto.reqres;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter


public class ReversalStatusWebhookresult {
	
	private String mid;
	private String orderId;
	private String paytmOrderId;
	private String amount;
	private String commissionAmount;
	private String tax;
	private String rrn;
	private String beneficiaryName;
	private String reversalReason;

}
