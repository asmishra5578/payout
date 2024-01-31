package io.asktech.payout.dto.reqres;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PayTmAddFundreq {

	private String subwalletGuid;
	private String amount;
	
}
