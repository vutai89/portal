package com.mcredit.model.enums;

public enum UplCreAppFileStatus {
	
	N("N"), // New case (when insert)
	X("X"), // files were extracted from zip
	F("F"), // files were converted to PDF
	P("P"), // files were uploaded to BPM
	;
	
	private final String value;

	UplCreAppFileStatus(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}
