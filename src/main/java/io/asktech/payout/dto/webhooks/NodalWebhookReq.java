package io.asktech.payout.dto.webhooks;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class NodalWebhookReq {
	private String event_tracking_id;
	private String ca_id;
	private NodalWebhookReqData Data;
}
