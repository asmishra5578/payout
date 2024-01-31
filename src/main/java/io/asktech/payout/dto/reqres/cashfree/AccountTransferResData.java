package io.asktech.payout.dto.reqres.cashfree;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter

public class AccountTransferResData {
	   private String utr;
	   private String acknowledged;
	   private String referenceId;
}
