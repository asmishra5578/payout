package io.asktech.payout.dto.reqres;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class MerAddFundreq {
	private String merchantId;
	private String orderId;
	private String subwalletGuid;
	private String amount;
	
}
