package io.asktech.payout.mainwallet.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainWalletReversalReqDto {
	private String amount;
	private String transactionType;
	private String purpose;
	private String remarks;
	
}
