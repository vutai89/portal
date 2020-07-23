package com.mcredit.model.dto.card;

import java.io.Serializable;
import java.util.Date;

public class CustomerCompanyInfoDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id;
	private Integer compAddrDistrict;
	private String compAddrDistrictValue;
	private Integer compAddrProvince;
	private String compAddrProvinceValue;
	private Integer compAddrWard;
	private String compAddrWardValue;
	private String compAddrStreet;
	private String compName;
	private String compTaxNumber;
	private String createdBy;
	private Date createdDate;
	private String lastUpdatedBy;
	private Date lastUpdatedDate;
	private String officePhoneNumber;
	private String recordStatus;
	private String custName;

	public CustomerCompanyInfoDTO() {
	}

	public String getCompAddrDistrictValue() {
		return compAddrDistrictValue;
	}

	public void setCompAddrDistrictValue(String compAddrDistrictValue) {
		this.compAddrDistrictValue = compAddrDistrictValue;
	}

	public String getCompAddrProvinceValue() {
		return compAddrProvinceValue;
	}

	public void setCompAddrProvinceValue(String compAddrProvinceValue) {
		this.compAddrProvinceValue = compAddrProvinceValue;
	}

	public String getCompAddrWardValue() {
		return compAddrWardValue;
	}

	public void setCompAddrWardValue(String compAddrWardValue) {
		this.compAddrWardValue = compAddrWardValue;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCompAddrDistrict() {
		return this.compAddrDistrict;
	}

	public void setCompAddrDistrict(Integer compAddrDistrict) {
		this.compAddrDistrict = compAddrDistrict;
	}

	public Integer getCompAddrProvince() {
		return this.compAddrProvince;
	}

	public void setCompAddrProvince(Integer compAddrProvince) {
		this.compAddrProvince = compAddrProvince;
	}

	public String getCompAddrStreet() {
		return this.compAddrStreet;
	}

	public void setCompAddrStreet(String compAddrStreet) {
		this.compAddrStreet = compAddrStreet;
	}

	public Integer getCompAddrWard() {
		return this.compAddrWard;
	}

	public void setCompAddrWard(Integer compAddrWard) {
		this.compAddrWard = compAddrWard;
	}

	public String getCompName() {
		return this.compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public String getCompTaxNumber() {
		return this.compTaxNumber;
	}

	public void setCompTaxNumber(String compTaxNumber) {
		this.compTaxNumber = compTaxNumber;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdatedDate() {
		return this.lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getOfficePhoneNumber() {
		return this.officePhoneNumber;
	}

	public void setOfficePhoneNumber(String officePhoneNumber) {
		this.officePhoneNumber = officePhoneNumber;
	}

	public String getRecordStatus() {
		return this.recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

}