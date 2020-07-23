package com.mcredit.model.dto.check_cat;

import java.util.List;

public class SearchCompanyDTO {

	private String name;
	private List<String> taxNumbers;
	private String establishDate;
	private Long cicInfo;
	private String dateCheckCat;
	private String top500_1000;
	private String top100_1000_branch;
	private Long companyType;
	private Long economicType;
	private String isMultinationalCompany;
	private String operationStatus;
	private Long catType;
	private Integer pageNumber;
	private Integer pageSize;

	public SearchCompanyDTO() {} 
	
	public SearchCompanyDTO(String name, List<String> taxNumber, String establishDate, 
			Long cicInfo, String dateCheckCat, String top500_1000, 
			String top100_1000_branch, Long companyType, Long economicType,
			String isMultinationalCompany, String operationStatus,  Long catType, 
			Integer pageNumber,  Integer pageSize
		) {
		this.name = name;
		this.taxNumbers = taxNumber;
		this.establishDate = establishDate;
		this.cicInfo = cicInfo;
		this.dateCheckCat = dateCheckCat;
		this.top500_1000 = top500_1000;
		this.top100_1000_branch = top100_1000_branch;
		this.companyType = companyType;
		this.economicType = economicType;
		this.isMultinationalCompany = isMultinationalCompany;
		this.operationStatus = operationStatus;
		this.catType = catType;
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getTaxNumbers() {
		return taxNumbers;
	}

	public void setTaxNumbers(List<String> taxNumbers) {
		this.taxNumbers = taxNumbers;
	}

	public String getDateCheckCat() {
		return dateCheckCat;
	}

	public void setDateCheckCat(String dateCheckCat) {
		this.dateCheckCat = dateCheckCat;
	}

	public Long getCatType() {
		return catType;
	}

	public void setCatType(Long catType) {
		this.catType = catType;
	}

	public String getEstablishDate() {
		return establishDate;
	}

	public void setEstablishDate(String establishDate) {
		this.establishDate = establishDate;
	}

	public Long getCicInfo() {
		return cicInfo;
	}

	public void setCicInfo(Long cicInfo) {
		this.cicInfo = cicInfo;
	}

	public Long getCompanyType() {
		return companyType;
	}

	public void setCompanyType(Long companyType) {
		this.companyType = companyType;
	}

	public Long getEconomicType() {
		return economicType;
	}

	public void setEconomicType(Long economicType) {
		this.economicType = economicType;
	}

	public String getOperationStatus() {
		return operationStatus;
	}

	public void setOperationStatus(String operationStatus) {
		this.operationStatus = operationStatus;
	}

	public String getTop500_1000() {
		return top500_1000;
	}

	public void setTop500_1000(String top500_1000) {
		this.top500_1000 = top500_1000;
	}

	public String getTop100_1000_branch() {
		return top100_1000_branch;
	}

	public void setTop100_1000_branch(String top100_1000_branch) {
		this.top100_1000_branch = top100_1000_branch;
	}

	public String getIsMultinationalCompany() {
		return isMultinationalCompany;
	}

	public void setIsMultinationalCompany(String isMultinationalCompany) {
		this.isMultinationalCompany = isMultinationalCompany;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}
