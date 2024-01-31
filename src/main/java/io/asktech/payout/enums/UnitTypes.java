package io.asktech.payout.enums;

import java.util.HashMap;
import java.util.Map;

public enum UnitTypes {
    PERCENTAGE("PERCENTAGE"),UNIT("UNIT");
	
	private static Map<String, Object> map = new HashMap<>();
	private String unitTypes;
	static {
		for (UnitTypes unitTypes : UnitTypes.values()) {
			map.put(unitTypes.unitTypes, unitTypes);
		}
	}

	public String getValue() {
		return unitTypes;
	}


	private UnitTypes(String k) {
		this.unitTypes = k;
	}

	public String getStatus() {
		return unitTypes;
	}
}
