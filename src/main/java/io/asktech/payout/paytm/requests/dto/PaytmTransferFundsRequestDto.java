package io.asktech.payout.paytm.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaytmTransferFundsRequestDto {
	
	private String subwalletGuid;
	private String amount;

}
