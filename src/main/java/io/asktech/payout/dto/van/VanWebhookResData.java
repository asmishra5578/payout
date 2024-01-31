package io.asktech.payout.dto.van;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class VanWebhookResData {

	
	private String status;
	private String bankTxnIdentifier;
	private String remitterIfsc;
}
