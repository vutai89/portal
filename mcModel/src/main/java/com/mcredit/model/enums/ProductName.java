package com.mcredit.model.enums;

public enum ProductName {
	
	SUB_TW("TW");

	private final String value;

	ProductName(String value) {
		this.value = value;
	}

	public static ProductName from(String text) {
		for (ProductName b : ProductName.values()) {
			if (b.value.equalsIgnoreCase(text)) {
				return b;
			}
		}
		return null;
	}

	public String value() {
		return this.value;
	}
}