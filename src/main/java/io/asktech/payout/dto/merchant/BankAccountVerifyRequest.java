package io.asktech.payout.dto.merchant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankAccountVerifyRequest {

	private String orderId;
	private String internalOrderId;
	private String beneficiaryAccountNo;
	private String beneficiaryIFSCCode;
	private String merchantId;
	private String beneficiaryVPA;
}
