package com.mcredit.model.dto.check_cat;

public class ResultCheckCatDTO {
	
	private String compName;
	private String catType;
	private String compAddrStreet;
	private String officePhoneNumber;
	
	public ResultCheckCatDTO() {
	}
	
	public ResultCheckCatDTO(String compName, String catType) {
		this.compName = compName;
		this.catType = catType;
	}
	
	public String getCatType() {
		return catType;
	}

	public void setCatType(String catType) {
		this.catType = catType;
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public String getCompAddrStreet() {
		return compAddrStreet;
	}

	public void setCompAddrStreet(String compAddrStreet) {
		this.compAddrStreet = compAddrStreet;
	}

	public String getOfficePhoneNumber() {
		return officePhoneNumber;
	}

	public void setOfficePhoneNumber(String officePhoneNumber) {
		this.officePhoneNumber = officePhoneNumber;
	}

	
}
