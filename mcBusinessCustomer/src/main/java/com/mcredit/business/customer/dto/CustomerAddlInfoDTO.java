package com.mcredit.business.customer.dto;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

import com.mcredit.sharedbiz.validation.DateTimeDeserialize;

public class CustomerAddlInfoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1457935732002392211L;
	
	private Long id;
	private Integer accommodationType;
	private String accommodationTypeValue;
	private Integer blackListType;
	private String blackListTypeValue;
	private String createdBy;
	private Date createdDate;
	private Integer custCompanyId;
	private String custCompanyIdValue;
	private Long custId;
	private String department;
	private Integer education;
	private String educationValue;
	private Integer incomeSpouse;
	private Integer isBlackList;
	private String isBlackListValue;
	private Integer labourContractType;
	private String labourContractTypeValue;
	private String lastUpdatedBy;
	private Date lastUpdatedDate;
	private Integer lifetimeInMonth;
	private Integer lifetimeInYear;
	private String militaryId;
	
	@JsonDeserialize(using=DateTimeDeserialize.class)
	private Date militaryIssueDate;
	
	private String militaryIssuePlace;
	private Integer monthExperience;
	private Integer numberOfDependants;
	private String oldIdentityNumber;
	private Integer payrollMethod;
	private String payrollMethodValue;
	private Integer positionInComp;
	private String positionInCompValue;
	private Integer professional;
	private String professionalValue;
	private String recordStatus;
	private String refFullName1;
	private String refFullName2;
	private String refPerson1Mobile;
	private String refPerson2Mobile;
	private Integer relationRefPerson1;
	private String relationRefPerson1Value;
	private Integer relationRefPerson2;
	private String relationRefPerson2Value;
	private Integer relationSpouse;
	private String relationSpouseValue;
	private String spouseCompanyName;
	
	@JsonDeserialize(using=DateTimeDeserialize.class)
	private Date spouseDob;
	
	private String spouseIdentityNumber;
	private String spouseMobile;
	private String spouseName;
	private Integer spousePosition;
	private String spousePositionValue;
	private Integer tempSamePermAddr;
	private String tempSamePermAddrValue;
	private Integer yearExperience;

	public CustomerAddlInfoDTO() {
	}

	public String getAccommodationTypeValue() {
		return accommodationTypeValue;
	}

	public void setAccommodationTypeValue(String accommodationTypeValue) {
		this.accommodationTypeValue = accommodationTypeValue;
	}

	public String getBlackListTypeValue() {
		return blackListTypeValue;
	}

	public void setBlackListTypeValue(String blackListTypeValue) {
		this.blackListTypeValue = blackListTypeValue;
	}

	public String getCustCompanyIdValue() {
		return custCompanyIdValue;
	}

	public void setCustCompanyIdValue(String custCompanyIdValue) {
		this.custCompanyIdValue = custCompanyIdValue;
	}

	public String getEducationValue() {
		return educationValue;
	}

	public void setEducationValue(String educationValue) {
		this.educationValue = educationValue;
	}

	public String getIsBlackListValue() {
		return isBlackListValue;
	}

	public void setIsBlackListValue(String isBlackListValue) {
		this.isBlackListValue = isBlackListValue;
	}

	public String getLabourContractTypeValue() {
		return labourContractTypeValue;
	}

	public void setLabourContractTypeValue(String labourContractTypeValue) {
		this.labourContractTypeValue = labourContractTypeValue;
	}

	public String getPayrollMethodValue() {
		return payrollMethodValue;
	}

	public void setPayrollMethodValue(String payrollMethodValue) {
		this.payrollMethodValue = payrollMethodValue;
	}

	public String getPositionInCompValue() {
		return positionInCompValue;
	}

	public void setPositionInCompValue(String positionInCompValue) {
		this.positionInCompValue = positionInCompValue;
	}

	public String getProfessionalValue() {
		return professionalValue;
	}

	public void setProfessionalValue(String professionalValue) {
		this.professionalValue = professionalValue;
	}

	public String getRelationRefPerson1Value() {
		return relationRefPerson1Value;
	}

	public void setRelationRefPerson1Value(String relationRefPerson1Value) {
		this.relationRefPerson1Value = relationRefPerson1Value;
	}

	public String getRelationRefPerson2Value() {
		return relationRefPerson2Value;
	}

	public void setRelationRefPerson2Value(String relationRefPerson2Value) {
		this.relationRefPerson2Value = relationRefPerson2Value;
	}

	public String getRelationSpouseValue() {
		return relationSpouseValue;
	}

	public void setRelationSpouseValue(String relationSpouseValue) {
		this.relationSpouseValue = relationSpouseValue;
	}

	public String getSpousePositionValue() {
		return spousePositionValue;
	}

	public void setSpousePositionValue(String spousePositionValue) {
		this.spousePositionValue = spousePositionValue;
	}

	public String getTempSamePermAddrValue() {
		return tempSamePermAddrValue;
	}

	public void setTempSamePermAddrValue(String tempSamePermAddrValue) {
		this.tempSamePermAddrValue = tempSamePermAddrValue;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getAccommodationType() {
		return this.accommodationType;
	}

	public void setAccommodationType(Integer accommodationType) {
		this.accommodationType = accommodationType;
	}

	public Integer getBlackListType() {
		return this.blackListType;
	}

	public void setBlackListType(Integer blackListType) {
		this.blackListType = blackListType;
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

	public Integer getCustCompanyId() {
		return this.custCompanyId;
	}

	public void setCustCompanyId(Integer custCompanyId) {
		this.custCompanyId = custCompanyId;
	}

	public Long getCustId() {
		return this.custId;
	}

	public void setCustId(Long custId) {
		this.custId = custId;
	}

	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Integer getEducation() {
		return this.education;
	}

	public void setEducation(Integer education) {
		this.education = education;
	}

	public Integer getIncomeSpouse() {
		return this.incomeSpouse;
	}

	public void setIncomeSpouse(Integer incomeSpouse) {
		this.incomeSpouse = incomeSpouse;
	}

	public Integer getIsBlackList() {
		return this.isBlackList;
	}

	public void setIsBlackList(Integer isBlackList) {
		this.isBlackList = isBlackList;
	}

	public Integer getLabourContractType() {
		return this.labourContractType;
	}

	public void setLabourContractType(Integer labourContractType) {
		this.labourContractType = labourContractType;
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

	public Integer getLifetimeInMonth() {
		return this.lifetimeInMonth;
	}

	public void setLifetimeInMonth(Integer lifetimeInMonth) {
		this.lifetimeInMonth = lifetimeInMonth;
	}

	public Integer getLifetimeInYear() {
		return this.lifetimeInYear;
	}

	public void setLifetimeInYear(Integer lifetimeInYear) {
		this.lifetimeInYear = lifetimeInYear;
	}

	public String getMilitaryId() {
		return this.militaryId;
	}

	public void setMilitaryId(String militaryId) {
		this.militaryId = militaryId;
	}

	public Date getMilitaryIssueDate() {
		return this.militaryIssueDate;
	}

	public void setMilitaryIssueDate(Date militaryIssueDate) {
		this.militaryIssueDate = militaryIssueDate;
	}

	public String getMilitaryIssuePlace() {
		return this.militaryIssuePlace;
	}

	public void setMilitaryIssuePlace(String militaryIssuePlace) {
		this.militaryIssuePlace = militaryIssuePlace;
	}

	public Integer getMonthExperience() {
		return this.monthExperience;
	}

	public void setMonthExperience(Integer monthExperience) {
		this.monthExperience = monthExperience;
	}

	public Integer getNumberOfDependants() {
		return this.numberOfDependants;
	}

	public void setNumberOfDependants(Integer numberOfDependants) {
		this.numberOfDependants = numberOfDependants;
	}

	public String getOldIdentityNumber() {
		return this.oldIdentityNumber;
	}

	public void setOldIdentityNumber(String oldIdentityNumber) {
		this.oldIdentityNumber = oldIdentityNumber;
	}

	public Integer getPayrollMethod() {
		return this.payrollMethod;
	}

	public void setPayrollMethod(Integer payrollMethod) {
		this.payrollMethod = payrollMethod;
	}

	public Integer getPositionInComp() {
		return this.positionInComp;
	}

	public void setPositionInComp(Integer positionInComp) {
		this.positionInComp = positionInComp;
	}

	public Integer getProfessional() {
		return this.professional;
	}

	public void setProfessional(Integer professional) {
		this.professional = professional;
	}

	public String getRecordStatus() {
		return this.recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getRefFullName1() {
		return this.refFullName1;
	}

	public void setRefFullName1(String refFullName1) {
		this.refFullName1 = refFullName1;
	}

	public String getRefFullName2() {
		return this.refFullName2;
	}

	public void setRefFullName2(String refFullName2) {
		this.refFullName2 = refFullName2;
	}

	public String getRefPerson1Mobile() {
		return this.refPerson1Mobile;
	}

	public void setRefPerson1Mobile(String refPerson1Mobile) {
		this.refPerson1Mobile = refPerson1Mobile;
	}

	public String getRefPerson2Mobile() {
		return this.refPerson2Mobile;
	}

	public void setRefPerson2Mobile(String refPerson2Mobile) {
		this.refPerson2Mobile = refPerson2Mobile;
	}

	public Integer getRelationRefPerson1() {
		return this.relationRefPerson1;
	}

	public void setRelationRefPerson1(Integer relationRefPerson1) {
		this.relationRefPerson1 = relationRefPerson1;
	}

	public Integer getRelationRefPerson2() {
		return this.relationRefPerson2;
	}

	public void setRelationRefPerson2(Integer relationRefPerson2) {
		this.relationRefPerson2 = relationRefPerson2;
	}

	public Integer getRelationSpouse() {
		return this.relationSpouse;
	}

	public void setRelationSpouse(Integer relationSpouse) {
		this.relationSpouse = relationSpouse;
	}

	public String getSpouseCompanyName() {
		return this.spouseCompanyName;
	}

	public void setSpouseCompanyName(String spouseCompanyName) {
		this.spouseCompanyName = spouseCompanyName;
	}

	public Date getSpouseDob() {
		return this.spouseDob;
	}

	public void setSpouseDob(Date spouseDob) {
		this.spouseDob = spouseDob;
	}

	public String getSpouseIdentityNumber() {
		return this.spouseIdentityNumber;
	}

	public void setSpouseIdentityNumber(String spouseIdentityNumber) {
		this.spouseIdentityNumber = spouseIdentityNumber;
	}

	public String getSpouseMobile() {
		return this.spouseMobile;
	}

	public void setSpouseMobile(String spouseMobile) {
		this.spouseMobile = spouseMobile;
	}

	public String getSpouseName() {
		return this.spouseName;
	}

	public void setSpouseName(String spouseName) {
		this.spouseName = spouseName;
	}

	public Integer getSpousePosition() {
		return this.spousePosition;
	}

	public void setSpousePosition(Integer spousePosition) {
		this.spousePosition = spousePosition;
	}

	public Integer getTempSamePermAddr() {
		return this.tempSamePermAddr;
	}

	public void setTempSamePermAddr(Integer tempSamePermAddr) {
		this.tempSamePermAddr = tempSamePermAddr;
	}

	public Integer getYearExperience() {
		return this.yearExperience;
	}

	public void setYearExperience(Integer yearExperience) {
		this.yearExperience = yearExperience;
	}

}