package com.mcredit.model.enums;

public enum CompNameHavingEnums {
	COMP_NAME_HAVE("COMP_NAME_HAVE"),
	COMP_NAME_HAVE_2("COMP_NAME_HAVE_2"),
	COMP_NAME_NOTHAVE("COMP_NAME_NOTHAVE");
	
	private final String value;

	CompNameHavingEnums(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

}