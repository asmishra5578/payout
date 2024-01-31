package io.asktech.payout.dto.reqres.cashfree;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter

public class UPITransferResData {
	   private String utr;
	   private String acknowledged;
	   private String referenceId;
}
