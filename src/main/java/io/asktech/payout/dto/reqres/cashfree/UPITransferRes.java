package io.asktech.payout.dto.reqres.cashfree;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UPITransferRes {
	   private UPITransferResData data;
	   private String subCode;
	   private String message;
	   private String status;
}
