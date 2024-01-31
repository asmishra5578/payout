package io.asktech.payout.wallet.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalletRechargeResDto {
	private String amount;
	private String status;
	private String purpose;
	private String transactionId;
	private String credit_debit;
	private String closingBalance;
	private String remark;
	private String mainWalletId;
	private String callBackURL;
}
