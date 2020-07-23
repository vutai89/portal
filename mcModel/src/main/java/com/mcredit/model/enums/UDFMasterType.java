package com.mcredit.model.enums;

public enum UDFMasterType {

	CUSTOMER("C"),
	APPLICATION("A"),
	DOCUMENT("D"),
	PROSPECT_CUSTOMER("P");

	private final String value;

	UDFMasterType(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}
