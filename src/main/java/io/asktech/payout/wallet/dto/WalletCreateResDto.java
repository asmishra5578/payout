package io.asktech.payout.wallet.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalletCreateResDto {
	private String walletId;
	private String walletStatus;
	private String responseStatus;
	private String name;
	private String amount;
	private String mainWalletId;
}
