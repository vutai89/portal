package com.mcredit.model.dto.appraisal;

public class GoodsInformation {
	
	private Double goodsPrice;			// gia tri hang hoa (GOODS_VALIDATION) (bpm FIX)
	private String typeOfGoods;			// loai hang hoa: GOODS45_XM, GOODS46_XDD_VINFAST, GOODS42_MOBILE, ... (bpm FIX)
	private String brand;				// nhan hieu hang hoa: APPLE, SAMSUNG, ... (bpm FIX)
	private String typeOfGoodsName;		// ten loai hang hoa
	private String brandName;			// ten nhan hieu hang hoa
	
	public Double getGoodsPrice() {
		return goodsPrice;
	}
	
	public void setGoodsPrice(Double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	
	public String getTypeOfGoods() {
		return typeOfGoods;
	}
	
	public void setTypeOfGoods(String typeOfGoods) {
		this.typeOfGoods = typeOfGoods;
	}
	
	public String getBrand() {
		return brand;
	}
	
	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getTypeOfGoodsName() {
		return typeOfGoodsName;
	}

	public void setTypeOfGoodsName(String typeOfGoodsName) {
		this.typeOfGoodsName = typeOfGoodsName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	
}
