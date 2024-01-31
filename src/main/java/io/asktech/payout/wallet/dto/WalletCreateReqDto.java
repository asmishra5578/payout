package io.asktech.payout.wallet.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalletCreateReqDto {
	String mainWalletid;
	String status;
	String amount;
	String name;
	String walletCallBackAPI;
}
