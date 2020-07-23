package com.mcredit.model.enums;

public enum RuleParameterListType {

	REFERENCE("REF"),
	DATE_EFFECTIVE_TEST("DET"),
	ARRAY_INPUT_TYPE("AIT"),
	NO_INPUT_MAP("NUL");

	private final String value;

	RuleParameterListType(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

}
