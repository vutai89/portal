package com.mcredit.model.enums.product;

public enum PageConfig {
	PageIndex(1),
	PageSize(10);
	
	private final Integer value;

	PageConfig(Integer value) {
		this.value = value;
	}

	public Integer value() {
		return this.value;
	}
}
