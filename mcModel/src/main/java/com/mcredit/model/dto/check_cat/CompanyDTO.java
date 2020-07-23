package com.mcredit.model.dto.check_cat;

public class CompanyDTO {

	private String compAddrStreet;

	private String compName;

	private String compTaxNumber;

	private String officePhoneNumber;
	
	private String establishDate;
	
	private String taxChapter;
	
	private String catType;
	
	private String compType;
	
	private String isTopComp;
	
	private String isTopBranch;
	
	private String isMultiComp;
	
	private String cicConsultingDate;
	
	private Integer operationMonth;
	
	private String cicInfo;
	
	private String economicType;
	
	private String cicCode;
	
	private String recordStatus;
	
	private String companyTypeDisplay;
	
	private String dateCheckCat;
	

	public String getDateCheckCat() {
		return dateCheckCat;
	}

	public void setDateCheckCat(String dateCheckCat) {
		this.dateCheckCat = dateCheckCat;
	}

	public String getCompAddrStreet() {
		return compAddrStreet;
	}

	public String getCompName() {
		return compName;
	}

	public String getCompTaxNumber() {
		return compTaxNumber;
	}

	public String getOfficePhoneNumber() {
		return officePhoneNumber;
	}

	public String getEstablishDate() {
		return establishDate;
	}

	public String getTaxChapter() {
		return taxChapter;
	}

	public String getCatType() {
		return catType;
	}

	public String getCompType() {
		return compType;
	}

	public String getIsTopComp() {
		return isTopComp;
	}

	public String getIsTopBranch() {
		return isTopBranch;
	}

	public String getIsMultiComp() {
		return isMultiComp;
	}

	public String getCicConsultingDate() {
		return cicConsultingDate;
	}

	public Integer getOperationMonth() {
		return operationMonth;
	}

	public String getCicInfo() {
		return cicInfo;
	}

	public String getEconomicType() {
		return economicType;
	}

	public String getCicCode() {
		return cicCode;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setCompAddrStreet(String compAddrStreet) {
		this.compAddrStreet = compAddrStreet;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public void setCompTaxNumber(String compTaxNumber) {
		this.compTaxNumber = compTaxNumber;
	}

	public void setOfficePhoneNumber(String officePhoneNumber) {
		this.officePhoneNumber = officePhoneNumber;
	}

	public void setEstablishDate(String establishDate) {
		this.establishDate = establishDate;
	}

	public void setTaxChapter(String taxChapter) {
		this.taxChapter = taxChapter;
	}

	public void setCatType(String catType) {
		this.catType = catType;
	}

	public void setCompType(String compType) {
		this.compType = compType;
	}

	public void setIsTopComp(String isTopComp) {
		this.isTopComp = isTopComp;
	}

	public void setIsTopBranch(String isTopBranch) {
		this.isTopBranch = isTopBranch;
	}

	public void setIsMultiComp(String isMultiComp) {
		this.isMultiComp = isMultiComp;
	}

	public void setCicConsultingDate(String cicConsultingDate) {
		this.cicConsultingDate = cicConsultingDate;
	}

	public void setOperationMonth(Integer operationMonth) {
		this.operationMonth = operationMonth;
	}

	public void setCicInfo(String cicInfo) {
		this.cicInfo = cicInfo;
	}

	public void setEconomicType(String economicType) {
		this.economicType = economicType;
	}

	public void setCicCode(String cicCode) {
		this.cicCode = cicCode;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getCompanyTypeDisplay() {
		return companyTypeDisplay;
	}

	public void setCompanyTypeDisplay(String companyTypeDisplay) {
		this.companyTypeDisplay = companyTypeDisplay;
	}
	
	@Override
	public String toString() {
		return "CompanyDTO [compAddrStreet=" + compAddrStreet + ", compName=" + compName + ", compTaxNumber="
				+ compTaxNumber + ", officePhoneNumber=" + officePhoneNumber + ", establishDate=" + establishDate
				+ ", taxChapter=" + taxChapter + ", catType=" + catType + ", compType=" + compType + ", isTopComp="
				+ isTopComp + ", isTopBranch=" + isTopBranch + ", isMultiComp=" + isMultiComp + ", cicConsultingDate="
				+ cicConsultingDate + ", operationMonth=" + operationMonth + ", cicInfo=" + cicInfo + ", economicType="
				+ economicType + ", cicCode=" + cicCode + ", recordStatus=" + recordStatus + ", companyTypeDisplay="
				+ companyTypeDisplay + ", dateCheckCat=" + dateCheckCat + "]";
	}
	
}
