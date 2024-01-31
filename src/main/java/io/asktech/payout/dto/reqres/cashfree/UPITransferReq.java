package io.asktech.payout.dto.reqres.cashfree;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class UPITransferReq {

	   private String amount;
	   private String transferMode;
	   private String transferId;
	   private UPITransferDataReq beneDetails;
	   private String remarks;
}
