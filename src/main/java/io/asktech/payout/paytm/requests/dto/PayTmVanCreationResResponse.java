package io.asktech.payout.paytm.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PayTmVanCreationResResponse {
	private String referenceId;
	private String active;
	private String beneficiaryName;
	private String ifscCode;
	private String bankName;
	
}
