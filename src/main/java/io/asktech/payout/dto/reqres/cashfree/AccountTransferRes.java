package io.asktech.payout.dto.reqres.cashfree;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountTransferRes {
	   private AccountTransferResData data;
	   private String subCode;
	   private String message;
	   private String status;
}
