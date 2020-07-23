package com.mcredit.model.enums;

public enum ECMEnum {	
	NEW_UPLOAD("N"),
	PROCESSING("P"),
	COMPLETED_GET_FILE("F"),
	UPLOADED("U"),
	DELETED("D"),
	ERROR("E"),
	REMOVE("R"),
	SUCCESS_MSG("SUCCESS"),
	ERROR_MSG("ERROR"),
	INSERT("INSERT"),
	UPDATE("UPDATE")
	;
	
	private final String value;

	ECMEnum(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
	
	public static ECMEnum from(String text) {
        for (ECMEnum b : ECMEnum.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
