package io.asktech.payout.paytm.requests.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayTmTransactionReportReq {
	
	private String subwalletGuid;
	private String fromDate;
	private String toDate;
	private long pageSize; 
}
