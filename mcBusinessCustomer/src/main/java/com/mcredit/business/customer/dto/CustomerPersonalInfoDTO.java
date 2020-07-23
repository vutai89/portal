package com.mcredit.business.customer.dto;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

import com.mcredit.sharedbiz.validation.DateTimeDeserialize;

public class CustomerPersonalInfoDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@JsonDeserialize(using=DateTimeDeserialize.class)
	private Date birthDate;
	
	private String coreCustCode;
	private String createdBy;
	private Date createdDate;
	private String custName;
	private Integer gender;
	private String genderValue;
	private String homePhone;
	
	@JsonDeserialize(using=DateTimeDeserialize.class)
	private Date householdRegIssueDate;
	
	private String householdRegIssuePlace;
	private String householdRegNumber;
	private Integer householdRegTypeId;
	private String householdRegTypeIdValue;
	private Long identityId;
	private String isProspect;
	private String lastUpdatedBy;
	private Date lastUpdatedDate;
	private Integer maritalStatus;
	private String maritalStatusValue;
	private String mcCustCode;
	private String otherCustName;
	private String recordStatus;
	private String shortCustName;

	public CustomerPersonalInfoDTO() {
	}
	
	public CustomerPersonalInfoDTO(String coreCustCode) {
		this.coreCustCode = coreCustCode;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getBirthDate() {
		return this.birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getCoreCustCode() {
		return this.coreCustCode;
	}

	public void setCoreCustCode(String coreCustCode) {
		this.coreCustCode = coreCustCode;
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

	public String getCustName() {
		return this.custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getHomePhone() {
		return this.homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public Date getHouseholdRegIssueDate() {
		return this.householdRegIssueDate;
	}

	public void setHouseholdRegIssueDate(Date householdRegIssueDate) {
		this.householdRegIssueDate = householdRegIssueDate;
	}

	public String getHouseholdRegIssuePlace() {
		return this.householdRegIssuePlace;
	}

	public void setHouseholdRegIssuePlace(String householdRegIssuePlace) {
		this.householdRegIssuePlace = householdRegIssuePlace;
	}

	public String getHouseholdRegNumber() {
		return this.householdRegNumber;
	}

	public void setHouseholdRegNumber(String householdRegNumber) {
		this.householdRegNumber = householdRegNumber;
	}

	public Long getIdentityId() {
		return this.identityId;
	}

	public void setIdentityId(Long identityId) {
		this.identityId = identityId;
	}

	public String getIsProspect() {
		return this.isProspect;
	}

	public void setIsProspect(String isProspect) {
		this.isProspect = isProspect;
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

	public String getMcCustCode() {
		return this.mcCustCode;
	}

	public void setMcCustCode(String mcCustCode) {
		this.mcCustCode = mcCustCode;
	}

	public String getOtherCustName() {
		return this.otherCustName;
	}

	public void setOtherCustName(String otherCustName) {
		this.otherCustName = otherCustName;
	}

	public String getRecordStatus() {
		return this.recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getShortCustName() {
		return this.shortCustName;
	}

	public void setShortCustName(String shortCustName) {
		this.shortCustName = shortCustName;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getGenderValue() {
		return genderValue;
	}

	public void setGenderValue(String genderValue) {
		this.genderValue = genderValue;
	}

	public Integer getHouseholdRegTypeId() {
		return householdRegTypeId;
	}

	public void setHouseholdRegTypeId(Integer householdRegTypeId) {
		this.householdRegTypeId = householdRegTypeId;
	}

	public String getHouseholdRegTypeIdValue() {
		return householdRegTypeIdValue;
	}

	public void setHouseholdRegTypeIdValue(String householdRegTypeIdValue) {
		this.householdRegTypeIdValue = householdRegTypeIdValue;
	}

	public Integer getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(Integer maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getMaritalStatusValue() {
		return maritalStatusValue;
	}

	public void setMaritalStatusValue(String maritalStatusValue) {
		this.maritalStatusValue = maritalStatusValue;
	}
}