package io.asktech.payout.wallet.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalletUpdateReqDto {

	private String merchantId;
	private String walletCallBackAPI;
	private String status;
	private String walletHoldAmount;
	private String instantReversal;
}
