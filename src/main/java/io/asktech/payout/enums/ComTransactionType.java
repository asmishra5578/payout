package io.asktech.payout.enums;

import java.util.HashMap;
import java.util.Map;

public enum ComTransactionType {
    UPI("UPI"),IMPS("IMPS"),NEFT("NEFT"),RTGS("RTGS");
	
	private static Map<String, Object> map = new HashMap<>();
	private String comTransactionType;
	static {
		for (ComTransactionType comTransactionType : ComTransactionType.values()) {
			map.put(comTransactionType.comTransactionType, comTransactionType);
		}
	}

	public String getValue() {
		return comTransactionType;
	}


	private ComTransactionType(String k) {
		this.comTransactionType = k;
	}

	public String getStatus() {
		return comTransactionType;
	}
}
