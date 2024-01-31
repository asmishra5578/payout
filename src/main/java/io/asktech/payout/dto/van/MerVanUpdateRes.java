package io.asktech.payout.dto.van;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class MerVanUpdateRes {

	private String merchantId;
	private String orderId;
	private String code;
	private String message;
	private String success ;
}
