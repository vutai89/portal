package com.mcredit.model.enums;

public enum UDFType {

	TEXTBOX("T"),
	MULTILINE_TEXTBOX("M"),
	DROPDOWN_LIST("D"),
	CHECKBOX("C"),
	OPTION_BUTTON("O");

	private final String value;

	UDFType(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

}
