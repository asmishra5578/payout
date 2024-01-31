package io.asktech.payout.paytm.requests.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaytmAccountVerifyResponse {
	private String status;
	private String statusCode;
	private String statusMessage;
}
