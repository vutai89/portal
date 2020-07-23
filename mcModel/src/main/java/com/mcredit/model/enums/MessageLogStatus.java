package com.mcredit.model.enums;

public enum MessageLogStatus {
	NEW("N"),// New
	ERROR("E"),// Run errors
	SUCCESS("S"),// Run success
	TIMEOUT("T"), // Request time out
	IGNORE("I"), // Ignore
	P("P")
	;

	private final String value;

	MessageLogStatus(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}
