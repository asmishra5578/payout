package io.asktech.payout.dto.reqres.cashfree;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationRes {
	   private AuthenticationResData data;
	   private String subCode;
	   private String message;
	   private String status;
}
