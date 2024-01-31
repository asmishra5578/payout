package io.asktech.payout.dto.webhooks;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class NodalWebhookReqDataExtInfo {

	private String transfer_mode;
	private String externalTransactionId;
}
