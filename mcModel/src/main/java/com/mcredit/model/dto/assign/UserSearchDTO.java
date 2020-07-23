package com.mcredit.model.dto.assign;

public class UserSearchDTO {
	private Long id;
	private String loginId;
	private String userFullName;
	private String hrCode;
	private String userType;
	private String email;
	private String mobilePhone;
	private String extPhone;
	private String deviceName;
	private String userStatus;
	private String createdDate;
	private String lastUpdatedDate;
	private String startEffDate;
	private String endEffDate;
	private Long userTypeId;
	private String external;
	private Long externalId;
	private String status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public String getHrCode() {
		return hrCode;
	}

	public void setHrCode(String hrCode) {
		this.hrCode = hrCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getEndEffDate() {
		return endEffDate;
	}

	public void setEndEffDate(String endEffDate) {
		this.endEffDate = endEffDate;
	}

	public Long getUserTypeId() {
		return userTypeId;
	}

	public void setUserTypeId(Long userTypeId) {
		this.userTypeId = userTypeId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getStartEffDate() {
		return startEffDate;
	}

	public void setStartEffDate(String startEffDate) {
		this.startEffDate = startEffDate;
	}

	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(String lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getExternal() {
		return external;
	}

	public void setExternal(String external) {
		this.external = external;
	}

	public Long getExternalId() {
		return externalId;
	}

	public void setExternalId(Long externalId) {
		this.externalId = externalId;
	}

	@Override
	public String toString() {
		return "UserSearchDTO [id=" + id + ", loginId=" + loginId + ", userFullName=" + userFullName + ", hrCode="
				+ hrCode + ", userType=" + userType + ", email=" + email + ", mobilePhone=" + mobilePhone
				+ ", extPhone=" + extPhone + ", deviceName=" + deviceName + ", userStatus=" + userStatus
				+ ", createdDate=" + createdDate + ", lastUpdatedDate=" + lastUpdatedDate + ", startEffDate="
				+ startEffDate + ", endEffDate=" + endEffDate + ", userTypeId=" + userTypeId + ", external=" + external
				+ ", externalId=" + externalId + ", status=" + status + "]";
	}
}
