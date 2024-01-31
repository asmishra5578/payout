package io.asktech.payout.dto.van;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class responseVANDto {

	private String referenceId;
	private String active;
	private String ifscCode;
	private String beneficiaryName;
	private String bankName;	
	
}
