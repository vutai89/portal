package com.mcredit.model.enums.product;

public enum SaleChannel {
	CD(2),
	TW(1);
	
	private final Integer value;

	SaleChannel(Integer value) {
		this.value = value;
	}

	public Integer value() {
		return this.value;
	}
}
