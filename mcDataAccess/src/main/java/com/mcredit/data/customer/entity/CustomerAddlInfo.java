package com.mcredit.data.customer.entity;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;


/**
 * The persistent class for the CUST_ADDL_INFO database table.
 * 
 */
@Entity
@Table(name="CUST_ADDL_INFO")
public class CustomerAddlInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "seq_cai"
				    , strategy = "com.mcredit.data.common.entity.SequenceGenerate"
					, parameters = @Parameter(name = "seqName", value = "SEQ_CUST_ADDL_INFO_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_cai")
	private Long id;

	@Column(name="ACCOMMODATION_TYPE")
	private Integer accommodationType;

	@Column(name="BLACK_LIST_TYPE")
	private Integer blackListType;

	@Column(name="CREATED_BY", updatable=false)
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DATE", updatable=false)
	@CreationTimestamp
	private Date createdDate;

	@Column(name="CUST_COMPANY_ID")
	private Long custCompanyId;

	@Column(name="CUST_ID")
	private Long custId;

	private String department;

	private Integer education;

	@Column(name="INCOME_SPOUSE")
	private Long incomeSpouse;

	@Column(name="IS_BLACK_LIST")
	private Integer isBlackList;

	@Column(name="LABOUR_CONTRACT_TYPE")
	private Integer labourContractType;

	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPDATED_DATE")
	@UpdateTimestamp
	private Date lastUpdatedDate;

	@Column(name="LIFETIME_IN_MONTH")
	private Integer lifetimeInMonth;

	@Column(name="LIFETIME_IN_YEAR")
	private Integer lifetimeInYear;

	@Column(name="MILITARY_ID")
	private String militaryId;

	@Temporal(TemporalType.DATE)
	@Column(name="MILITARY_ISSUE_DATE")
	private Date militaryIssueDate;

	@Column(name="MILITARY_ISSUE_PLACE")
	private String militaryIssuePlace;

	@Column(name="MONTH_EXPERIENCE")
	private Integer monthExperience;

	@Column(name="NUMBER_OF_DEPENDANTS")
	private Integer numberOfDependants;

	@Column(name="OLD_IDENTITY_NUMBER")
	private String oldIdentityNumber;

	@Column(name="PAYROLL_METHOD")
	private Integer payrollMethod;

	@Column(name="POSITION_IN_COMP")
	private Integer positionInComp;

	private Integer professional;

	@Column(name="RECORD_STATUS")
	private String recordStatus;

	@Column(name="REF_FULL_NAME1")
	private String refFullName1;

	@Column(name="REF_FULL_NAME2")
	private String refFullName2;

	@Column(name="REF_PERSON1_MOBILE")
	private String refPerson1Mobile;

	@Column(name="REF_PERSON2_MOBILE")
	private String refPerson2Mobile;

	@Column(name="RELATION_REF_PERSON1")
	private Integer relationRefPerson1;

	@Column(name="RELATION_REF_PERSON2")
	private Integer relationRefPerson2;

	@Column(name="RELATION_SPOUSE")
	private Integer relationSpouse;

	@Column(name="SPOUSE_COMPANY_NAME")
	private String spouseCompanyName;

	@Temporal(TemporalType.DATE)
	@Column(name="SPOUSE_DOB")
	private Date spouseDob;

	@Column(name="SPOUSE_IDENTITY_NUMBER")
	private String spouseIdentityNumber;

	@Column(name="SPOUSE_MOBILE")
	private String spouseMobile;

	@Column(name="SPOUSE_NAME")
	private String spouseName;

	@Column(name="SPOUSE_POSITION")
	private Integer spousePosition;

	@Column(name="TEMP_SAME_PERM_ADDR")
	private Integer tempSamePermAddr;

	@Column(name="YEAR_EXPERIENCE")
	private Integer yearExperience;

	public CustomerAddlInfo() {
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
		this.createdBy = createdBy!=null?createdBy.trim():null;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getCustCompanyId() {
		return this.custCompanyId;
	}

	public void setCustCompanyId(Long custCompanyId) {
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
		this.department = department!=null?department.trim():null;
	}

	public Integer getEducation() {
		return this.education;
	}

	public void setEducation(Integer education) {
		this.education = education;
	}

	public Long getIncomeSpouse() {
		return this.incomeSpouse;
	}

	public void setIncomeSpouse(Long incomeSpouse) {
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
		this.lastUpdatedBy = lastUpdatedBy!=null?lastUpdatedBy.trim():null;
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
		this.militaryId = militaryId!=null?militaryId.trim():null;
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
		this.militaryIssuePlace = militaryIssuePlace!=null?militaryIssuePlace.trim():null;
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
		this.oldIdentityNumber = oldIdentityNumber!=null?oldIdentityNumber.trim():null;
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
		this.recordStatus = recordStatus!=null?recordStatus.trim():null;
	}

	public String getRefFullName1() {
		return this.refFullName1;
	}

	public void setRefFullName1(String refFullName1) {
		this.refFullName1 = refFullName1!=null?refFullName1.trim():null;
	}

	public String getRefFullName2() {
		return this.refFullName2;
	}

	public void setRefFullName2(String refFullName2) {
		this.refFullName2 = refFullName2!=null?refFullName2.trim():null;
	}

	public String getRefPerson1Mobile() {
		return this.refPerson1Mobile;
	}

	public void setRefPerson1Mobile(String refPerson1Mobile) {
		this.refPerson1Mobile = refPerson1Mobile!=null?refPerson1Mobile.trim():null;
	}

	public String getRefPerson2Mobile() {
		return this.refPerson2Mobile;
	}

	public void setRefPerson2Mobile(String refPerson2Mobile) {
		this.refPerson2Mobile = refPerson2Mobile!=null?refPerson2Mobile.trim():null;
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
		this.spouseCompanyName = spouseCompanyName!=null?spouseCompanyName.trim():null;
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
		this.spouseIdentityNumber = spouseIdentityNumber!=null?spouseIdentityNumber.trim():null;
	}

	public String getSpouseMobile() {
		return this.spouseMobile;
	}

	public void setSpouseMobile(String spouseMobile) {
		this.spouseMobile = spouseMobile!=null?spouseMobile.trim():null;
	}

	public String getSpouseName() {
		return this.spouseName;
	}

	public void setSpouseName(String spouseName) {
		this.spouseName = spouseName!=null?spouseName.trim():null;
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