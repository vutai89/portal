package com.mcredit.data.user.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "EMPLOYEES")
public class Employee implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3366030501037680207L;

	@Id
	@GenericGenerator(name = "seq_Employee", strategy = "com.mcredit.data.common.entity.SequenceGenerate", parameters = @Parameter(name = "seqName", value = "SEQ_EMPLOYEES_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_Employee")
	@Column(name = "ID")
	private Long id;

	@Column(name = "RECORD_STATUS")
	private String recordStatus;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "LAST_UPDATED_DATE")
	private Date lastUpdatedDate;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Column(name = "FULL_NAME")
	private String fullName;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "HR_CODE")
	private String hrCode;

	@Column(name = "MOBILE_PHONE")
	private String mobilePhone;

	@Column(name = "EXT_PHONE")
	private String extPhone;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "START_EFF_DATE")
	private Date StartEffDate;

	@Column(name = "END_EFF_DATE")
	private Date endEffDate;

	@Column(name = "STAFF_ID_IN_BPM")
	private Long staffIdInBPM;

	@Transient
	private String loginId;

	@Transient
	private Long userId;
	
	@Transient
	private String departmentName;
	
	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHrCode() {
		return hrCode;
	}

	public void setHrCode(String hrCode) {
		this.hrCode = hrCode;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getExtPhone() {
		return extPhone;
	}

	public void setExtPhone(String extPhone) {
		this.extPhone = extPhone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStartEffDate() {
		return StartEffDate;
	}

	public void setStartEffDate(Date startEffDate) {
		StartEffDate = startEffDate;
	}

	public Date getEndEffDate() {
		return endEffDate;
	}

	public void setEndEffDate(Date endEffDate) {
		this.endEffDate = endEffDate;
	}

	public Long getStaffIdInBPM() {
		return staffIdInBPM;
	}

	public void setStaffIdInBPM(Long staffIdInBPM) {
		this.staffIdInBPM = staffIdInBPM;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
