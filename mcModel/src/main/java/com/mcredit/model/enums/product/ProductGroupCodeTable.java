package com.mcredit.model.enums.product;

public enum ProductGroupCodeTable {
	
	COMMODITY("INST"),
	PROD_GROUP("SC_GROUP"),
	PROD("PROD");
	
	private final String value;

	ProductGroupCodeTable(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}
