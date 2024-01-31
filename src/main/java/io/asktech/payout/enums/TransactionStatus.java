package io.asktech.payout.enums;

import java.util.HashMap;
import java.util.Map;

public enum TransactionStatus {

	UNAUTHORIZED("UNAUTHORIZED"),CANCELLED("CANCELLED"),SUCCESS("SUCCESS"),FAILURE("FAILURE"),PGNULL("PGNULL"),
	REVERSED("REVERSED"),FAILED("FAILED"),ERROR("ERROR"),PENDING("PENDING"),ACCEPTED("ACCEPTED");
	
	private static Map<String, Object> map = new HashMap<>();
	private String transactionStatus;
	static {
		for (TransactionStatus transactionStatus : TransactionStatus.values()) {
			map.put(transactionStatus.transactionStatus, transactionStatus);
		}
	}

	public String getValue() {
		return transactionStatus;
	}


	private TransactionStatus(String k) {
		this.transactionStatus = k;
	}

	public String getStatus() {
		return transactionStatus;
	}
}
