package io.asktech.payout.wallet.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NofityResponse2Merchant {

	private String creationDate;
	private String amount;
	private String bankAccount;
	private String beneficiaryName;
	private String ifscCode;
	private String phonenumber;
	private String purpose;
	private String transactionMessage;
	private String transactionStatus;
	private String transactionType;
	private String beneficiaryVPA;
	private String utrid;
	private String merchantOrderId;
	private String pgOrderId;
	
}
