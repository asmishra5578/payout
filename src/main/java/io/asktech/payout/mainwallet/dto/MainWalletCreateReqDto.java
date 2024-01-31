package io.asktech.payout.mainwallet.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainWalletCreateReqDto {
	private String status;
	private String name;
	private String amount;
}
