package io.asktech.payout.dto.reqres;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AddFundDetail {

	private String subwalletGuid;
	private String amount;
	private String statusCode;
	private String status;
	private String statusMessage;
	
}
