package com.mcredit.model.enums;

public enum UDFDataType {

	STRING("S"),
	DATE("D"),
	TIME("T"),
	LONG("L"),
	INTEGER("I"),
	DECIMAL("N"),
	FLOAT("F"),
	DOUBLE("U"),
	BOOLEAN("B"),
	DATETIME("M");

	private final String value;

	UDFDataType(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

}
