package com.mcredit.model.enums.product;

public enum ActionData {
	
	Create("N"),
	Update("U"),
	Active("A"),
	InActive("I"),
	Delete("D");
	
	private final String value;

	ActionData(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}
