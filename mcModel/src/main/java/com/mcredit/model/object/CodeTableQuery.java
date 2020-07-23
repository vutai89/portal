package com.mcredit.model.object;

import java.io.Serializable;

public class CodeTableQuery implements Serializable {

	private String category;
	private String productCode;
	private String productGroup;
	private String productCategory;
	private String ruleCode;
	private String codeValue1;
	private String codeValue2;

	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getRuleCode() {
		return ruleCode;
	}
	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}
	public String getCodeValue1() {
		return codeValue1;
	}
	public void setCodeValue1(String codeValue1) {
		this.codeValue1 = codeValue1;
	}
	public String getCodeValue2() {
		return codeValue2;
	}
	public void setCodeValue2(String codeValue2) {
		this.codeValue2 = codeValue2;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductGroup() {
		return productGroup;
	}
	public void setProductGroup(String productGroup) {
		this.productGroup = productGroup;
	}
	public String getProductCategory() {
		return productCategory;
	}
	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

}
