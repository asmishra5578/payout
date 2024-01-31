package io.asktech.payout.dto.nodal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class MerNodalBenefManagementReq {
	
		private String merchantId;
		private String orderId;
		private String beneficiaryAccountNumber;
		private String beneficiaryIfsc;
		private String beneficiaryAccountType;
		private String beneficiaryUserType;
		private String beneficiaryName;
		private String beneficiaryEmail;
		private String beneficiaryMobile;
		private String beneficiaryNickName;

}
