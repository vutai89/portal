package com.mcredit.model.dto.warehouse;

import java.util.Date;

public class EmployeeDTO {
	private Long id;

	private String recordStatus;

	private Date createdDate;

	private Date lastUpdatedDate;

	private String createdBy;

	private String lastUpdatedBy;

	private String fullName;

	private String email;

	private String hrCode;

	private String mobilePhone;

	private String extPhone;

	private String status;

	private Date StartEffDate;

	private Date endEffDate;

	private Long staffIdInBPM;

	private String loginId;
	
	private Long userId;
	
	private String departmentName;

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

}
