package io.asktech.payout.dto.merchant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionReportMerReqResult {
	private String utrId;
	private String txnDate;
	private String txnId;
	private String merchantId;
	private String orderId;
	private String creditOrDebit;
	private String beneficiary;
	private String txnAmount;
	private String txnStatus;
	private String txnType;
	private String merchantTxnType;
}
