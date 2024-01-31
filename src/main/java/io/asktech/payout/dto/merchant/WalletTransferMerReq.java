package io.asktech.payout.dto.merchant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalletTransferMerReq {
	private String orderid;
	private String internalOrderId;
	private String phonenumber;
	private String amount;
	private String pgName;
	private String pgId;
}
