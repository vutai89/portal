package com.mcredit.model.enums.product;

public enum MapType {
	
	CommodityBrand("MAP_COMM_BRAND"),
	CommodityBrandModel("MAP_COMM_BRAND_MODEL"),
	ProductCommodity("MAP_PROD_COMM"),
	ProductCommodityBrand("MAP_PROD_COMM_BRAND"),
	SchemeCommTW("MAP_SCHEME_COMM_TW"),
	SchemeBrandTW("MAP_SCHEME_BRAND_TW"),
	Product_ProductGroup("MAP_SCHEME_PRDGROUP"),
	Comm_ProductGroup("MAP_COMM_PRDGROUP"),
	Comm_Channel("MAP_COMM_CHANNEL"),
	Comm_Prod_Comm_Bpm("MAP_PROD_COMM_BPM");
	
	private final String value;

	MapType(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}
