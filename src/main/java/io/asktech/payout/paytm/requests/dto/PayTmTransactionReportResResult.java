package io.asktech.payout.paytm.requests.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayTmTransactionReportResResult {
	private int startResult;
	private int resultSize;
	private int endResult;
	private List<PayTmTransactionReportResResultResult> result;
}
