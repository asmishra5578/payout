package io.asktech.payout.dto.merchant;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class NodalBalTransRes {

	private String status;
	private String amount;
	private String response_code;
	private String txn_id;
	private String target_account;
	private NodalBalTransResextra_info extra_info;
}
