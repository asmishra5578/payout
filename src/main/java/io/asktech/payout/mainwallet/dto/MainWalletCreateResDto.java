package io.asktech.payout.mainwallet.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainWalletCreateResDto {
	private String walletId;
	private String walletStatus;
	private String responseStatus;
	private String name;
	private String amount;
}
