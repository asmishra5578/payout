package io.asktech.payout.dto.merchant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BalanceCheckMerRes {
	private String status;
	private String message;
	private String walletBalance;
	private String lastUpdatedDate;
	private String merchantId;
	private String walletId;	
}
