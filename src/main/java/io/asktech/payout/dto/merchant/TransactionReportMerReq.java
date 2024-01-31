package io.asktech.payout.dto.merchant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionReportMerReq {
	private String fromDate;
	private String toDate;
	private String transactionType;
}
