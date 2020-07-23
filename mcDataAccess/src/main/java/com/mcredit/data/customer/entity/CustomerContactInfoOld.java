package com.mcredit.data.customer.entity;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;


/**
 * The persistent class for the CUST_CONTACT_INFO database table.
 * 
 */
@Entity
@Table(name="CUST_CONTACT_INFO_OLD")
public class CustomerContactInfoOld implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "seq_cci"
				    , strategy = "com.mcredit.data.common.entity.SequenceGenerate"
					, parameters = @Parameter(name = "seqName", value = "SEQ_CUST_CONTACT_INFO_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_cci")
	private Long id;

	@Column(name="CREATED_BY", updatable=false)
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DATE", updatable=false)
	@CreationTimestamp
	private Date createdDate;

	@Column(name="CURRENT_ADDR_SPOUSE")
	private Integer currentAddrSpouse;

	@Column(name="CUST_ID")
	private Long custId;

	@Column(name="EMAIL")
	private String email;

	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPDATED_DATE")
	@UpdateTimestamp
	private Date lastUpdatedDate;

	@Column(name="MOBILE")
	private String mobile;

	@Column(name="PERMANENT_ADDR")
	private String permanentAddr;

	@Column(name="PERMANENT_DISTRICT")
	private Integer permanentDistrict;

	@Column(name="PERMANENT_PROVINCE")
	private Integer permanentProvince;

	@Column(name="PERMANENT_WARD")
	private Integer permanentWard;

	@Column(name="RECORD_STATUS")
	private String recordStatus;

	@Column(name="SPOUSE_ADDR")
	private String spouseAddr;

	@Column(name="SPOUSE_DISTRICT")
	private Integer spouseDistrict;

	@Column(name="SPOUSE_PROVINCE")
	private Integer spouseProvince;

	@Column(name="SPOUSE_WARD")
	private Integer spouseWard;

	@Column(name="TEMP_ADDR")
	private String tempAddr;

	@Column(name="TEMP_DISTRICT")
	private Integer tempDistrict;

	@Column(name="TEMP_PROVINCE")
	private Integer tempProvince;

	@Column(name="TEMP_WARD")
	private Integer tempWard;

	public CustomerContactInfoOld() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Integer getCurrentAddrSpouse() {
		return this.currentAddrSpouse;
	}

	public void setCurrentAddrSpouse(Integer currentAddrSpouse) {
		this.currentAddrSpouse = currentAddrSpouse;
	}

	public Long getCustId() {
		return this.custId;
	}

	public void setCustId(Long custId) {
		this.custId = custId;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email!=null?email.trim():null;
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

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile!=null?mobile.trim():null;
	}

	public String getPermanentAddr() {
		return this.permanentAddr;
	}

	public void setPermanentAddr(String permanentAddr) {
		this.permanentAddr = permanentAddr!=null?permanentAddr.trim():null;
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

	public String getRecordStatus() {
		return this.recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus!=null?recordStatus.trim():null;
	}

	public String getSpouseAddr() {
		return this.spouseAddr;
	}

	public void setSpouseAddr(String spouseAddr) {
		this.spouseAddr = spouseAddr!=null?spouseAddr.trim():null;
	}

	public Integer getSpouseDistrict() {
		return this.spouseDistrict;
	}

	public void setSpouseDistrict(Integer spouseDistrict) {
		this.spouseDistrict = spouseDistrict;
	}

	public Integer getSpouseProvince() {
		return this.spouseProvince;
	}

	public void setSpouseProvince(Integer spouseProvince) {
		this.spouseProvince = spouseProvince;
	}

	public Integer getSpouseWard() {
		return this.spouseWard;
	}

	public void setSpouseWard(Integer spouseWard) {
		this.spouseWard = spouseWard;
	}

	public String getTempAddr() {
		return this.tempAddr;
	}

	public void setTempAddr(String tempAddr) {
		this.tempAddr = tempAddr!=null?tempAddr.trim():null;
	}

	public Integer getTempDistrict() {
		return this.tempDistrict;
	}

	public void setTempDistrict(Integer tempDistrict) {
		this.tempDistrict = tempDistrict;
	}

	public Integer getTempProvince() {
		return this.tempProvince;
	}

	public void setTempProvince(Integer tempProvince) {
		this.tempProvince = tempProvince;
	}

	public Integer getTempWard() {
		return this.tempWard;
	}

	public void setTempWard(Integer tempWard) {
		this.tempWard = tempWard;
	}

}