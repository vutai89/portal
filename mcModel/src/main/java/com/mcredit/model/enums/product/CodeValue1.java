package com.mcredit.model.enums.product;

public enum CodeValue1 {
	
	ProductGroupOther("OTHER");
	
	private final String value;

	CodeValue1(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}
