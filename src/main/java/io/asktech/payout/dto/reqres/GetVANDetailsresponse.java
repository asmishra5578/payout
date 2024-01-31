package io.asktech.payout.dto.reqres;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GetVANDetailsresponse {

	private String productNumber;
	private String referenceId;
	private String partnerRefId;
	private String name;
	private String mobileNumber;
	private String solution;
	private String active;
	private String createdAt;
	private String updatedAt;
	private String createdBy;
	private String updatedBy;
	private String productStatus;
	private String ifscCode;
	
}
