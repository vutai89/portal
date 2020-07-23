package com.mcredit.model.enums;

public enum AutoAbortStsEnum {

	OPEN("O"),
	PENDING("P"),
	ABORT("C"),
	ERROR("E"),
	REJECT("R");
	
	
	private final String value;
	
	AutoAbortStsEnum(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}
