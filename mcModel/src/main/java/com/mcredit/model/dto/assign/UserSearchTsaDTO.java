package com.mcredit.model.dto.assign;

public class UserSearchTsaDTO {
	private String id;
	private String loginId;
	private String userFullName;
	private String hrCode;
	private String userType;
	private String email;
	private String mobilePhone;
	private String extPhone;
	private String userStatus;
	private String teamName;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
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
	

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	@Override
	public String toString() {
		return "UserSearchTsaDTO [loginId=" + loginId + ", userFullName=" + userFullName + ", hrCode=" + hrCode
				+ ", userType=" + userType + ", email=" + email + ", mobilePhone=" + mobilePhone + ", extPhone="
				+ extPhone + ", userStatus=" + userStatus + ", teamName=" + teamName + "]";
	}
	
}
