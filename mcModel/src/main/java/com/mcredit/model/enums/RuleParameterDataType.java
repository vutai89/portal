package com.mcredit.model.enums;

public enum RuleParameterDataType {

	STRING("S"),
	DATE("D"),
	TIME("T"),
	LONG("L"),
	INTEGER("I"),
	DECIMAL("N"),
	FLOAT("F"),
	DOUBLE("U"),
	BOOLEAN("B"),
	DATETIME("M"),
	LIST("A");		// list value

	private final String value;

	RuleParameterDataType(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

}
