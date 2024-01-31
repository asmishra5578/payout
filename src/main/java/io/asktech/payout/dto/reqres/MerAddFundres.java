package io.asktech.payout.dto.reqres;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class MerAddFundres {
	
	private String merchantId;
	private String orderId;
	private String statusCode;
	private String status;
	private String statusMessage;
	
}
