package com.mcredit.model.enums;

public enum IdentityType {
	
	CMND("1");

	private final String value;

	IdentityType(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}
