package com.mcredit.model.enums;

public enum EconomicTypeCheckEnums {
	DV_NH("nop ho"),
	DV_NT("nop thay"),
	CQNGQT("co quan ngoai giao to chuc quoc te");
	
	private final String value;

	EconomicTypeCheckEnums(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}
