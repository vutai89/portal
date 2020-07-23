package com.mcredit.model.enums;

public enum VendorCodeEnum {
	
	TS("TS"),
	CICDATA("CICDATA")
	;
	
	private final String value;
	
	VendorCodeEnum(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
	
}
