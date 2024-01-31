package io.asktech.payout.dto.reqres;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransferReq {
	
	private String requestId;
	private String amount;
	private String mobile;
	private String merchantID;

}
