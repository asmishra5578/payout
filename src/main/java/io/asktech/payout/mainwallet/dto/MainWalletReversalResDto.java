package io.asktech.payout.mainwallet.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainWalletReversalResDto {
	private String amount;
	private String status;
	private String purpose;
	private String transactionId;
	private String credit_debit;
	private String closingBalance;
	private String remark;
}
