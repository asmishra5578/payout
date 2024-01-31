package io.asktech.payout.dto.reqres;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ListVAN {

	private String productNumber;
	private String referenceId;
	private String partnerRefId;
	private String name;
	private String mobileNumber;
	private String solution;
	private String active;
	private String createdAt;
	private String updatedBy;
	private String ifscCode;
	private ListVANmeta meta;
	
}
