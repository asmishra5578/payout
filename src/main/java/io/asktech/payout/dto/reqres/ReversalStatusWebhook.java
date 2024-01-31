package io.asktech.payout.dto.reqres;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ReversalStatusWebhook {

private String status;
private String statusCode;
private String statusMessage;
ReversalStatusWebhookresult result;

}
