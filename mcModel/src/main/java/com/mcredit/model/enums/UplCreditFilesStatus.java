package com.mcredit.model.enums;

public enum UplCreditFilesStatus {
	N("N"),// New file insert
	X("X"),// Files were extracted from zip
	F("F"),// Files were converted to PDF
	P("P"),// File were uploaded to BPM
	;

	private final String value;

	UplCreditFilesStatus(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

}
