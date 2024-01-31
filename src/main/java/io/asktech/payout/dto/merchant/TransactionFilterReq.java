package io.asktech.payout.dto.merchant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionFilterReq {
	private String fromDate;
	private String toDate;
	private String bankaccount;
	private String beneficiaryName;
	private String ifsc;
	private String merchantId;
	private String transactionId;
	private String orderId;
	private String status;
	private String transactionType;
}
