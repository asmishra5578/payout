package io.asktech.payout.enums;

import java.util.HashMap;
import java.util.Map;

public enum SlabType {
	slab1("1_10000"),slab2("10001_25000"),slab3("25001_");
	
	private static Map<String, Object> map = new HashMap<>();
	private String slabType;
	static {
		for (SlabType slabType : SlabType.values()) {
			map.put(slabType.slabType, slabType);
		}
	}

	public String getValue() {
		return slabType;
	}


	private SlabType(String k) {
		this.slabType = k;
	}

	public String getStatus() {
		return slabType;
	}
}
