package io.asktech.payout.dto.nodal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class MerNodalBalTransRes {

	private String merchantId;
	private String orderId;
	private String status;
	private String amount;
	private String response_code;
	private String txn_id;
	private String target_account;
	private MerNodalBalTransResextra_info extra_info;
}
