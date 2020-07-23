package com.mcredit.model.enums;

public enum CacheTableName {
	PARAMETERS("PARAMETERS"),
	CACHE_CHANGE("CACHE_CHANGE"),
	CODE_TABLE("CODE_TABLE"),
	PARTNER("PARTNER"),
	POSTING_CONFIGURATION("POSTING_CONFIGURATION"),
	PRODUCTS("PRODUCTS"),
	SYSTEM_DEFINE_FIELDS("SYSTEM_DEFINE_FIELDS");
	
	private final String value;

	CacheTableName(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
	
	public static CacheTableName from(String text) {
        for (CacheTableName b : CacheTableName.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}