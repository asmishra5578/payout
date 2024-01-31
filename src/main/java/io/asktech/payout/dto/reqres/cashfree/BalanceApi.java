package io.asktech.payout.dto.reqres.cashfree;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BalanceApi {
	   private BalanceApiData data;
	   private Long subCode;
	   private String message;
	   private String status;

}
