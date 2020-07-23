package com.mcredit.model.enums;

public enum RuleParameterType {

	QUERY_SINGLE_VALUE("S"),
	QUERY_TABLE_VALUE("T"),
	QUERY_LIST_VALUE("Q"),
	QUERY_ROW_VALUE("R"),
	PREDEFINE_LIST_VALUE("L"),
	CONSTANT("C"),
	FORMULA("F"),
	JAVA_VARIABLE("J");

	private final String value;

	RuleParameterType(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

}
