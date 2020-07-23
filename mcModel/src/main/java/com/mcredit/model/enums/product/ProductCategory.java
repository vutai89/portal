package com.mcredit.model.enums.product;

public enum ProductCategory {
	
	ProductGroup("PRD_GR"),
	Commodity("COMM"),
	Goods("GOODS"),
	Brand("BRAND"),
	Model("MODEL"),
	Other("OTHER");
	
	private final String value;

	ProductCategory(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}
