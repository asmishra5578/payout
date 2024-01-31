package io.asktech.payout.paytm.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaytmWalletTransferRequestDto {
	private String orderId;
	private String subwalletGuid;
	private String amount;
	private String beneficiaryPhoneNo;
	
}
