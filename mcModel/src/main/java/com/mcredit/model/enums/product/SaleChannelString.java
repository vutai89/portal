package com.mcredit.model.enums.product;

public enum SaleChannelString {
	CD("E%"),
	TW("M%");
	
	private final String value;

	SaleChannelString(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}
