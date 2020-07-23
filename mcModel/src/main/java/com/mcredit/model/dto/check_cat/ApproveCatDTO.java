package com.mcredit.model.dto.check_cat;

public class ApproveCatDTO {
	private Long id;
	private String compName;
	private String taxNumber;
	private String compAddrStreet;
	private String officePhoneNumber;
	private String lastUpdatedDate;
	private String lastUpdatedBy;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCompName() {
		return compName;
	}
	public void setCompName(String compName) {
		this.compName = compName;
	}
	public String getTaxNumber() {
		return taxNumber;
	}
	public void setTaxNumber(String taxNumber) {
		this.taxNumber = taxNumber;
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
	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(String lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
}