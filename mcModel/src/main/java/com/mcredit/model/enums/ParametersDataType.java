package com.mcredit.model.enums;

public enum ParametersDataType {
	
	STRING("S"),
	
	INTERGER("I"),
	
	LONG("L"),
	
	BIGDECIMAL("N"),
	
	DATE("D"),
	
	TIME("T");
	
	private final String value;

	ParametersDataType(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}
