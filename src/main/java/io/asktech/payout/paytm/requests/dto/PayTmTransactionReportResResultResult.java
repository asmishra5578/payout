package io.asktech.payout.paytm.requests.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayTmTransactionReportResResultResult {
	private String merchantDisplayName;
	private String tds;
	private String txnDate;
	private String txnId;
	private String businessTxnType;
	private String merchantId;
	private String orderId;
	private String tax;
	private String subWalletName;
	private String merchantTxnType;
	private String txnType;
	private String paytmOrderID;
	private String purpose;
	private String creditOrDebit;
	private String mode;
	private String commission;
	private String beneficiary;
	private String txnAmount;
	private String closingBalance;
	private String txnStatus;
}
