package com.mcredit.model.enums;

public enum StatusBPM {
	DONE("DONE");

	private final String value;

	StatusBPM(String value) {
		this.value = value;
	}

	public static StatusBPM from(String text) {
		for (StatusBPM b : StatusBPM.values()) {
			if (b.value.equalsIgnoreCase(text)) {
				return b;
			}
		}
		return null;
	}

	public String value() {
		return this.value;
	}
}
