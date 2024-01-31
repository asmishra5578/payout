package io.asktech.payout.dto.merchant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountTransferUPIMerReq {

	private String orderid;
	private String internalOrderid;
	private String phonenumber;
	private String amount;
	private String purpose;
	private String beneficiaryName;
	private String beneficiaryVPA;
	private String requestType;
	private String pgName;
	private String reqStatus;
	private String trRemark;
	private String callBackURL;
	private int merchantThreadFlag;
	private String pgId;
}
