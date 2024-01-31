package io.asktech.payout.paytm.requests.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayTmBalanceCheckRes {
	private String statusCode;
	private String status;
	private String statusMessage;
	private List<PayTmBalanceCheckResults> result;
}
