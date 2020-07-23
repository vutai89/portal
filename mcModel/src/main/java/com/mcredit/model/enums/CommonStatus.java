package com.mcredit.model.enums;

public enum CommonStatus {
	NEW_SHORT("N"),
	PROCESSING_SHORT("P"),
	SUCCESS_SHORT("S"),
	ERROR_SHORT("E"),
	TIME_OUT_SHORT("T"),
	IGNORE_SHORT("I"),
	NEW("NEW"),
	CANCEL("CANCEL"),
	PROCESSING("PROCESSING"),
	CHECKED("CHECKED"),
	SUCCESS("SUCCESS"),
	ERROR("ERROR"),
	TIME_OUT("TIME_OUT"),
	IGNORE("IGNORE");
	
	private final String value;

	CommonStatus(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
	
	public static CommonStatus from(String text) {
        for (CommonStatus b : CommonStatus.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
