package com.mcredit.model.enums;

public enum UplCreditAppDocumentStatus {
	F("F"),// Files were converted to PDF
	P("P"),// File were uploaded to BPM
	;

	private final String value;

	UplCreditAppDocumentStatus(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

}
