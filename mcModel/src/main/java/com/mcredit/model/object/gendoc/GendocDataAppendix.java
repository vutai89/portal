package com.mcredit.model.object.gendoc;

public class GendocDataAppendix {
	private String goodCode;
	private String goodCodeOld;
	private String goodName;
	private String goodNameOld;
	private String brandCode;
	private String brandCodeOld;
	private String brandName;
	private String brandNameOld;
	private String modelCode;
	private String modelCodeOld;
	private String modelName;
	private String modelNameOld;

	public String getGoodCode() {
		return goodCode;
	}

	public void setGoodCode(String goodCode) {
		this.goodCode = goodCode;
	}

	public String getGoodCodeOld() {
		return goodCodeOld;
	}

	public void setGoodCodeOld(String goodCodeOld) {
		this.goodCodeOld = goodCodeOld;
	}

	public String getGoodName() {
		return goodName;
	}

	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}

	public String getGoodNameOld() {
		return goodNameOld;
	}

	public void setGoodNameOld(String goodNameOld) {
		this.goodNameOld = goodNameOld;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getBrandCodeOld() {
		return brandCodeOld;
	}

	public void setBrandCodeOld(String brandCodeOld) {
		this.brandCodeOld = brandCodeOld;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getBrandNameOld() {
		return brandNameOld;
	}

	public void setBrandNameOld(String brandNameOld) {
		this.brandNameOld = brandNameOld;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getModelCodeOld() {
		return modelCodeOld;
	}

	public void setModelCodeOld(String modelCodeOld) {
		this.modelCodeOld = modelCodeOld;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getModelNameOld() {
		return modelNameOld;
	}

	public void setModelNameOld(String modelNameOld) {
		this.modelNameOld = modelNameOld;
	}

	public GendocDataAppendix(String goodCode, String goodCodeOld, String goodName, String goodNameOld,
			String brandCode, String brandCodeOld, String brandName, String brandNameOld, String modelCode,
			String modelCodeOld, String modelName, String modelNameOld) {
		super();
		this.goodCode = goodCode;
		this.goodCodeOld = goodCodeOld;
		this.goodName = goodName;
		this.goodNameOld = goodNameOld;
		this.brandCode = brandCode;
		this.brandCodeOld = brandCodeOld;
		this.brandName = brandName;
		this.brandNameOld = brandNameOld;
		this.modelCode = modelCode;
		this.modelCodeOld = modelCodeOld;
		this.modelName = modelName;
		this.modelNameOld = modelNameOld;
	}

	public GendocDataAppendix() {
		super();
	}

}
