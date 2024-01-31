package io.asktech.payout.paytm.requests.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayTmTransactionReportRes {
	private String status;
	private String statusMessage;
	private String statusCode;
	private PayTmTransactionReportResResult result;
	
}
