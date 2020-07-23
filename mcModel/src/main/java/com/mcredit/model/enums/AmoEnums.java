package com.mcredit.model.enums;

public enum AmoEnums {
	
	REJECT("rejected"),
	PASSED("passed"),
	DEDUP_1("dedup1"),
	DEDUP_2("dedup2"),
	CUST_IN_BLACK_LIST("blacklist"),
	ERROR("error"),
	BLACK_LIST("B"),
	WATCH_LIST("W"),
	OK("OK"),
	UPL_STATUS("L"),
	PREFIX_UPL_CODE("AMO-"),
	AMO("AMO"),
	SUCCESS("success");
	
	private final String value;

	AmoEnums(String value) {
		this.value = value;
	}
	
	public static AmoEnums fromString(String text) {
	    for (AmoEnums b : AmoEnums.values()) {
	      if (b.value.equalsIgnoreCase(text))
	        return b;
	    }
	    return null;
	}
	
	public String value() {
		return this.value;
	}
}
