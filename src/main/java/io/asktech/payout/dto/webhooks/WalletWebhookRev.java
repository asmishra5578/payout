package io.asktech.payout.dto.webhooks;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class WalletWebhookRev {

	private String status;
	private String statusCode;
	private String statusMessage;
	private WalletWebhookRevResult Result;
	
}
