package io.asktech.payout.dto.reqres.cashfree;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class UPITransferDataReq {
	private String phone;
	   private String vpa;
	   private String address1;
	   private String name;
	   private String email;
}
