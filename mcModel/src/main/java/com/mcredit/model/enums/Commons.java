package com.mcredit.model.enums;

public enum Commons {
	
	CUSTOMER_CODE_PREFIX("MC-"),
	CUSTOMER_CODE_REMOTE(""),
	TONG_DU_NO(100000000),
	KY_TRA_NO(4),
	TS_UPL_SOURCE("TELESALE"),	
	AMO_UPL_SOURCE("AMO"),
	TC_UPL_SOURCE("TC"),
	SLASH("/"),
	BACKSLASH("\\"),
	DOT("."),
	MIS_MB_UPL_SOURCE("MB");
	
	private String value;
	private Integer intValue;
	
	Commons(String value) {
		this.value = value;
	}
	
	Commons(Integer intValue) {
		this.intValue = intValue;
	}

	public String value() {
		return this.value;
	}
	
	public Integer intValue() {
		return this.intValue;
	}
    
	public static Commons fromString(String text) {
		for (Commons b : Commons.values()) {
			if (b.value.equalsIgnoreCase(text))
				return b;
		}
		return null;
	}
	
	public static boolean contains(String value) {

	    for (Commons c : Commons.values()) {
	        if (c.value().equals(value)) {
	            return true;
	        }
	    }

	    return false;
	}
}
