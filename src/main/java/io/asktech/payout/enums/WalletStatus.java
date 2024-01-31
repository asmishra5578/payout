package io.asktech.payout.enums;

import java.util.HashMap;
import java.util.Map;


public enum WalletStatus {
	SUCCESS("SUCCESS"),FAILED("FAILED");
	
	private static Map<String, Object> map = new HashMap<>();
	private String walletStatus;
	static {
		for (WalletStatus walletStatus : WalletStatus.values()) {
			map.put(walletStatus.walletStatus, walletStatus);
		}
	}

	public String getValue() {
		return walletStatus;
	}


	private WalletStatus(String k) {
		this.walletStatus = k;
	}

	public String getStatus() {
		return walletStatus;
	}
}
