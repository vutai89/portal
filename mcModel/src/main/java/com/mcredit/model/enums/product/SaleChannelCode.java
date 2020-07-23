package com.mcredit.model.enums.product;

public enum SaleChannelCode {
	CD("CD"),
	TW("TW");
	
	private final String value;

	SaleChannelCode(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}
