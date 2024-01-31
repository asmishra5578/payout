package io.asktech.payout.dto.reqres;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferStatusResponce {

	   private String isCachedData;
	   private String beneficiaryIfsc;
	   private String amount;
	   private String reversalReason;
	   private String orderId;
	   private String paytmOrderId;
	   private String beneficiaryName;
	   private String mid;
	   private String tax;
	   private String cachedTime;
	   private String commissionAmount;
	   private String rrn;
	
}
