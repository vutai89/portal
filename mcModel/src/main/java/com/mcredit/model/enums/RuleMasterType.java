package com.mcredit.model.enums;

public enum RuleMasterType {

	VALIDATION("V"),
	PRE_CONDITION("C"),
	FORMULA("F"),
	LIST_VALUE("L"),
	PARENT("P"),
	SINGLE_VALUE("S"),
	DROP_LIST("D"),
	MULTI_VALUE("M"),
	ARRAY_INPUT("A");

	private final String value;

	RuleMasterType(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

}
