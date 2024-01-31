package io.asktech.payout.paytm.requests.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayTmBalanceCheckResults {
	private String walletType;
	private String subWalletGuid;
	private String walletName;
	private String walletBalance;
	private String currencyCode;
	private String lastUpdatedDate;
}
