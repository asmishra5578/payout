package io.asktech.payout.dto.van;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UpdateVANDetail {
	
	private String merchantId;
	private String id;
	private String status;
	private String type;
	private String code;
	private String message;
	
}
