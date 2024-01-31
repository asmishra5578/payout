package io.asktech.payout.enums;

import java.util.HashMap;
import java.util.Map;

public enum GatewayType {
    CASHFREE("CASHFREE");
	
	private static Map<String, Object> map = new HashMap<>();
	private String gatewayType;
	static {
		for (GatewayType gatewayType : GatewayType.values()) {
			map.put(gatewayType.gatewayType, gatewayType);
		}
	}

	public String getValue() {
		return gatewayType;
	}


	private GatewayType(String k) {
		this.gatewayType = k;
	}

	public String getStatus() {
		return gatewayType;
	}
}
