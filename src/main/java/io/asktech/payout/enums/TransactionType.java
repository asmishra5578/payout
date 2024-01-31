package io.asktech.payout.enums;

import java.util.HashMap;
import java.util.Map;

public enum TransactionType {
	ACCOUNT("ACCOUNT"),WALLET("WALLET"),VPA("VPA");
	
	private static Map<String, Object> map = new HashMap<>();
	private String transactionType;
	static {
		for (TransactionType transactionType : TransactionType.values()) {
			map.put(transactionType.transactionType, transactionType);
		}
	}

	public String getValue() {
		return transactionType;
	}


	private TransactionType(String k) {
		this.transactionType = k;
	}

	public String getStatus() {
		return transactionType;
	}
}
