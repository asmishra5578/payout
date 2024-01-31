package io.asktech.payout.paytm.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PayTMVanCreationRes {
	private String code;
	private String message;
	private String requestId;
	private PayTmVanCreationResResponse response;
	private String success;
	private String errorCode;
	private String errorMessage;

}
