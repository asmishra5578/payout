package io.asktech.payout.enums;

import java.util.HashMap;
import java.util.Map;

public enum WalletType {
	WALLET_TRANSFER("WALLET TRANSFER"),UPI("UPI"),IMPS("IMPS"),NEFT("NEFT"),BANK_TRANSFER("BANK TRANSFER");
	
	private static Map<String, Object> map = new HashMap<>();
	private String walletType;
	static {
		for (WalletType walletType : WalletType.values()) {
			map.put(walletType.walletType, walletType);
		}
	}

	public String getValue() {
		return walletType;
	}


	private WalletType(String k) {
		this.walletType = k;
	}

	public String getStatus() {
		return walletType;
	}
}
