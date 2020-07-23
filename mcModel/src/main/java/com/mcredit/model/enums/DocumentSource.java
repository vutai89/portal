package com.mcredit.model.enums;

public enum DocumentSource {

	COLLECTION("C"),
	DATA_CENTRALIZATION("D"),
	CASH("S"),
	INSTALLMENT("I"),
	FAS("F");

	private final String value;

	DocumentSource(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}
