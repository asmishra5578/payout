package io.asktech.payout.dto.van;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PayTmVanUpdateRes {

	private String code;
	private String message;
	private String status;
	private String requestId;
	private boolean success ;
}
