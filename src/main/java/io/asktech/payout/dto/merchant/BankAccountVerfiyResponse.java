package io.asktech.payout.dto.merchant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountVerfiyResponse {

	private String message;
	private String status;
	private String orderId;
}
