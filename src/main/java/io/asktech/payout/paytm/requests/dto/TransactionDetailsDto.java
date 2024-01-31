package io.asktech.payout.paytm.requests.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDetailsDto {
	String utrId;
	String trDateTime;
	String lastUpdate;
	String amount;
	String bankaccount;
	String beneficiaryName;
	String ifsc;
	String merchantId;
	String merchantOrderId;
	String phonenumber;
	String purpose;
	String transactionMessage;
	String transactionStatus;
	String transactionType;
	String beneficiaryVPA;
	String pgOrderId;
	String pgName;
	String referenceId;
	String errorMessage;
}
