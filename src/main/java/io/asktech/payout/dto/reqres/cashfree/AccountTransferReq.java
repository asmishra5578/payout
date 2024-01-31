package io.asktech.payout.dto.reqres.cashfree;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AccountTransferReq {
	   private String amount;
	   private String transferMode;
	   private String transferId;
	   private AccountTransferData beneDetails;
	   private String remarks;
}
