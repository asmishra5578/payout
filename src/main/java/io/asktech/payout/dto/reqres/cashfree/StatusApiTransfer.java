package io.asktech.payout.dto.reqres.cashfree;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusApiTransfer {

	   private String bankAccount;
	   private String amount;
	   private String utr;
	   private String processedOn;
	   private String acknowledged;
	   private String phone;
	   private String transferMode;
	   private String beneId;
	   private String transferId;
	   private String ifsc;
	   private String addedOn;
	   private String status;
	   private String vpa;
}
