package io.asktech.payout.dto.nodal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class NodalBenefManagementDetail {
	
	private String merchantId;
	private String beneficiaryAccountNumber;
	private String beneficiaryIfsc;
	private String beneficiaryAccountType;
	private String beneficiaryName;
	private String beneficiaryEmail;
	private String beneficiaryMobile;
	private String beneficiaryNickName;
	private String response;
	private String errorCode;
	private String status;
	private String message;

}
