package io.asktech.payout.dto.merchant;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccountTransferMerReq {
	private String orderid;
	private String internalOrderid;
	private String phonenumber;
	private String amount;
	private String bankaccount;
	private String ifsc;
	private String purpose;
	private String beneficiaryName;
	private String requestType;
	private String pgName;
	private String reqStatus;
	private String trRemark;
	private String callbackUrl;
	private int merchantThreadFlag;
	private String pgid;
}
