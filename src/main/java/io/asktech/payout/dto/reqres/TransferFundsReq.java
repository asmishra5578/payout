package io.asktech.payout.dto.reqres;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferFundsReq {
	
	private String amount;
	private String requestId;
	private String merchantId;
	

}
