package io.asktech.payout.dto.reqres.cashfree;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AccountTransferData {

	   private String phone;
	   private String address1;
	   private String bankAccount;
	   private String name;
	   private String ifsc;
	   private String email;
}
