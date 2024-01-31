package io.asktech.payout.dto.webhooks;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class NodalWebhookResData {

	private String transactionType;
	private String clientRequestId;
	private String transactionRequestId;
	
}
