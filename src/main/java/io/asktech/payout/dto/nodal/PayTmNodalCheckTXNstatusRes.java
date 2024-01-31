package io.asktech.payout.dto.nodal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PayTmNodalCheckTXNstatusRes {
	private String status;
	private String amount;
	private String transactionStatus;
	private int response_code;
	private String txn_id;
	private String message;
	private String rrn;
	private PayTmNodalCheckTXNstatusResextra_info extra_info;
	
}
