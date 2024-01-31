package io.asktech.payout.dto.reqres;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransferRes {
	private String statusCode;
	private String status;
	private String statusMessage;
	private String referenceId;
	private String utr;
	private String pgId;
	private String errorMsg;
}
