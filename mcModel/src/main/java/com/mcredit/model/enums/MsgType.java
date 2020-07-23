package com.mcredit.model.enums;

public enum MsgType {
	
	REQUEST("R"),
	REVERT("V");
	
	private final String value;

	MsgType(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}
