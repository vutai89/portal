package com.mcredit.data.customer.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.UpdateTimestamp;


/**
 * The persistent class for the CUST_PERSONAL_INFO database table.
 * 
 */
@Entity
@Table(name="CUST_PERSONAL_INFO")
public class CustomerPersonalInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5268544827817968152L;

	@Id
	@GenericGenerator(name = "seq_cpi"
				    , strategy = "com.mcredit.data.common.entity.SequenceGenerate"
					, parameters = @Parameter(name = "seqName", value = "SEQ_CUST_PERSONAL_INFO_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_cpi") 
	private Long id;

	@Temporal(TemporalType.DATE)
	@Column(name="BIRTH_DATE")
	private Date birthDate;

	@Column(name="CORE_CUST_CODE", updatable=false)
	private String coreCustCode;

	@Column(name="CREATED_BY", updatable=false)
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DATE", updatable=false)
	@CreationTimestamp
	private Date createdDate;

	@Column(name="CUST_NAME")
	private String custName;

	private Integer gender;

	@Column(name="HOME_PHONE")
	private String homePhone;

	@Temporal(TemporalType.DATE)
	@Column(name="HOUSEHOLD_REG_ISSUE_DATE")
	private Date householdRegIssueDate;

	@Column(name="HOUSEHOLD_REG_ISSUE_PLACE")
	private String householdRegIssuePlace;

	@Column(name="HOUSEHOLD_REG_NUMBER")
	private String householdRegNumber;

	@Column(name="HOUSEHOLD_REG_TYPE_ID")
	private Integer householdRegTypeId;

	@Column(name="IDENTITY_ID")
	private Long identityId;

	@Column(name="IS_PROSPECT")
	private String isProspect;

	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPDATED_DATE")
	@UpdateTimestamp
	private Date lastUpdatedDate;

	@Column(name="MARITAL_STATUS")
	private Integer maritalStatus;

	@Column(name="MC_CUST_CODE", updatable=false)
	private String mcCustCode;

	@Column(name="OTHER_CUST_NAME")
	private String otherCustName;

	@Column(name="RECORD_STATUS")
	private String recordStatus;

	@Column(name="SHORT_CUST_NAME")
	private String shortCustName;

	public CustomerPersonalInfo() {
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
		this.coreCustCode = coreCustCode!=null?coreCustCode.trim():null;
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

	public String getCustName() {
		return this.custName;
	}

	public void setCustName(String custName) {
		this.custName = custName!=null?custName.trim():null;
	}

	public Integer getGender() {
		return this.gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getHomePhone() {
		return this.homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone!=null?homePhone.trim():null;
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
		this.householdRegIssuePlace = householdRegIssuePlace!=null?householdRegIssuePlace.trim():null;
	}

	public String getHouseholdRegNumber() {
		return this.householdRegNumber;
	}

	public void setHouseholdRegNumber(String householdRegNumber) {
		this.householdRegNumber = householdRegNumber!=null?householdRegNumber.trim():null;
	}

	public Integer getHouseholdRegTypeId() {
		return this.householdRegTypeId;
	}

	public void setHouseholdRegTypeId(Integer householdRegTypeId) {
		this.householdRegTypeId = householdRegTypeId;
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
		this.isProspect = isProspect!=null?isProspect.trim():null;
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

	public Integer getMaritalStatus() {
		return this.maritalStatus;
	}

	public void setMaritalStatus(Integer maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getMcCustCode() {
		return this.mcCustCode;
	}

	public void setMcCustCode(String mcCustCode) {
		this.mcCustCode = mcCustCode!=null?mcCustCode.trim():null;
	}

	public String getOtherCustName() {
		return this.otherCustName;
	}

	public void setOtherCustName(String otherCustName) {
		this.otherCustName = otherCustName!=null?otherCustName.trim():null;
	}

	public String getRecordStatus() {
		return this.recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus!=null?recordStatus.trim():null;
	}

	public String getShortCustName() {
		return this.shortCustName;
	}

	public void setShortCustName(String shortCustName) {
		this.shortCustName = shortCustName!=null?shortCustName.trim():null;
	}
}