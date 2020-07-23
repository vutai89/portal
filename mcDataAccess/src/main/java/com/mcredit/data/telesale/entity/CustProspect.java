package com.mcredit.data.telesale.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * The persistent class for the CUST_PROSPECT database table.
 * 
 */
@Entity
@Table(name = "CUST_PROSPECT")
@NamedQuery(name = "CustProspect.findAll", query = "SELECT c FROM CustProspect c")
public class CustProspect implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "seq_cust_prospect_Id", strategy = "com.mcredit.data.common.entity.SequenceGenerate", parameters = @Parameter(name = "seqName", value = "SEQ_CUST_PROSPECT_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_cust_prospect_Id")
	private Long id;

	@Column(name = "ACCOMMODATION_TYPE")
	private Integer accommodationType;

	@Column(name = "ALLOCATION_DETAIL_ID")
	private Long allocationDetailId;

	@Temporal(TemporalType.DATE)
	@Column(name = "BIRTH_DATE")
	private Date birthDate;

	@Column(name = "COMP_ADDR_DISTRICT")
	private Integer compAddrDistrict;

	@Column(name = "COMP_ADDR_PROVINCE")
	private Integer compAddrProvince;

	@Column(name = "COMP_ADDR_STREET")
	private String compAddrStreet;

	@Column(name = "COMP_ADDR_WARD")
	private Integer compAddrWard;

	@Column(name = "CREATED_BY", updatable = false)
	private String createdBy;

	@CreationTimestamp
	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_DATE", updatable = false)
	private Date createdDate;

	@Column(name = "CUST_INCOME")
	private Long custIncome;

	@Column(name = "CUST_NAME")
	private String custName;

	private Integer gender;

	@Column(name = "IDENTITY_NUMBER")
	private String identityNumber;

	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@UpdateTimestamp
	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_UPDATED_DATE")
	private Date lastUpdatedDate;

	private String mobile;

	@Column(name = "NOTE")
	private String note;

	@Column(name = "PERMANENT_ADDR")
	private String permanentAddr;

	@Column(name = "PERMANENT_DISTRICT")
	private Integer permanentDistrict;

	@Column(name = "PERMANENT_PROVINCE")
	private Integer permanentProvince;

	@Column(name = "PERMANENT_WARD")
	private Integer permanentWard;

	private Integer professional;

	@Column(name = "RECORD_STATUS")
	private String recordStatus;

	@Column(name = "UPL_CUSTOMER_ID")
	private Long uplCustomerId;

	@Column(name = "POSITION")
	private Integer position;

	@Column(name = "PERMANENT_ADDRESS")
	private String permanentAddress;
	
	@Column(name = "NEW_MOBILE")
	private String newMobile;

	public String getPermanentAddress() {
		return permanentAddress;
	}

	public void setPermanentAddress(String permanentAddress) {
		this.permanentAddress = permanentAddress;
	}

	public CustProspect() {
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getAccommodationType() {
		return this.accommodationType;
	}

	public void setAccommodationType(Integer accommodationType) {
		this.accommodationType = accommodationType;
	}

	public Long getAllocationDetailId() {
		return this.allocationDetailId;
	}

	public void setAllocationDetailId(Long allocationDetailId) {
		this.allocationDetailId = allocationDetailId;
	}

	public Date getBirthDate() {
		return this.birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
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

	public Long getCustIncome() {
		return this.custIncome;
	}

	public void setCustIncome(Long custIncome) {
		this.custIncome = custIncome;
	}

	public String getCustName() {
		return this.custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public Integer getGender() {
		return this.gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getIdentityNumber() {
		return this.identityNumber;
	}

	public void setIdentityNumber(String identityNumber) {
		this.identityNumber = identityNumber;
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

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getPermanentAddr() {
		return this.permanentAddr;
	}

	public void setPermanentAddr(String permanentAddr) {
		this.permanentAddr = permanentAddr;
	}

	public Integer getPermanentDistrict() {
		return this.permanentDistrict;
	}

	public void setPermanentDistrict(Integer permanentDistrict) {
		this.permanentDistrict = permanentDistrict;
	}

	public Integer getPermanentProvince() {
		return this.permanentProvince;
	}

	public void setPermanentProvince(Integer permanentProvince) {
		this.permanentProvince = permanentProvince;
	}

	public Integer getPermanentWard() {
		return this.permanentWard;
	}

	public void setPermanentWard(Integer permanentWard) {
		this.permanentWard = permanentWard;
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

	public Long getUplCustomerId() {
		return this.uplCustomerId;
	}

	public void setUplCustomerId(Long uplCustomerId) {
		this.uplCustomerId = uplCustomerId;
	}

	public String getNewMobile() {
		return newMobile;
	}

	public void setNewMobile(String newMobile) {
		this.newMobile = newMobile;
	}

}