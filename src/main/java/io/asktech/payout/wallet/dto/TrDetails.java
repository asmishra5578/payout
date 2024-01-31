package io.asktech.payout.wallet.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrDetails {
	private String openingBalance;
	private String closingBalance;
	private String creditDebit;
	private String walletId;
	private String transactionStatus;
	private String statusRemarks;
	private String mainWalletId;
	private String referenceId;
	
}
