package com.mcredit.model.object.mobile.dto;

public class CategoryDTO {
	private String compName;
	private String catType;
	private String compAddrStreet;
	private String officeNumber;
	private String companyTaxNumber;

	public String getCompanyTaxNumber() {
		return companyTaxNumber;
	}
	public void setCompanyTaxNumber(String companyTaxNumber) {
		this.companyTaxNumber = companyTaxNumber;
	}
	public String getOfficeNumber() {
		return officeNumber;
	}
	public void setOfficeNumber(String officeNumber) {
		this.officeNumber = officeNumber;
	}
	public CategoryDTO(){}
	public String getCompName() {
		return compName;
	}
	public void setCompName(String compName) {
		this.compName = compName;
	}
	public String getCatType() {
		return catType;
	}
	public void setCatType(String catType) {
		this.catType = catType;
	}
	public String getCompAddrStreet() {
		return compAddrStreet;
	}
	public void setCompAddrStreet(String compAddrStreet) {
		this.compAddrStreet = compAddrStreet;
	}
}
