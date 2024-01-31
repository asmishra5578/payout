package io.asktech.payout.dto.reqres;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VanUpdateReq {
	private String orderid;
	private String status;
	private String merchantid; 
}
