package com.mcredit.model.object.mobile.enums;

public enum CaseStatusEnum {
	
	PROCESSING("PROCESSING"),
	ABORT("ABORT");
	
	private final String value;
	CaseStatusEnum(String value) {
		this.value = value;
	}
	
	public String value() {
		return this.value;
	}
	
	public static CaseStatusEnum from(String text) {
        for (CaseStatusEnum b : CaseStatusEnum.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
	
}
