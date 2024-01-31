package io.asktech.payout.enums;

import java.util.HashMap;
import java.util.Map;

public enum ReconStatus {

	R0("R0"),R1("R1"),R2("R2"),R3("R3"),R4("R4"),R5("R5"),R6("R6"),R("R"),RH("RH");
	
	private static Map<String, Object> map = new HashMap<>();
	private String reconStatus;
	static {
		for (ReconStatus reconStatus : ReconStatus.values()) {
			map.put(reconStatus.reconStatus, reconStatus);
		}
	}

	public String getValue() {
		return reconStatus;
	}


	private ReconStatus(String k) {
		this.reconStatus = k;
	}

	public String getStatus() {
		return reconStatus;
	}
}
