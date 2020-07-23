package com.mcredit.model.enums;

public enum CacheTag {
	CODE_TABLE("CODE_TABLE"),
	PARAMETERS("PARAMETERS"),
	PARTNER("PARTNER"),
	POSTING_CONFIGURATION("POSTING_CONFIGURATION"),
	PRODUCTS("PRODUCTS"),
	SYSTEM_DEFINE_FIELDS("SYSTEM_DEFINE_FIELDS"),
	RULE("RULE"),
	USER_PERMISSION("USER_PERMISSION"),
	COMPANY("COMPANY"),
	DOCUMENTS("DOCUMENT"),
	ALL("ALL");
	
	private String value;
	
	CacheTag(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
    
	public static CacheTag from(String text) {
		for (CacheTag b : CacheTag.values()) {
			if (b.value.equalsIgnoreCase(text))
				return b;
		}
		return null;
	}
	
	public static boolean contains(String value) {

	    for (CacheTag c : CacheTag.values()) {
	        if (c.value().equals(value)) {
	            return true;
	        }
	    }

	    return false;
	}
}
