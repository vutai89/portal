package com.mcredit.model.dto.assign;

public class UserListDTO {
	
	private String loginId;
	private String fullName;
	private String external;
	private String userType;
	
	public UserListDTO() {
	}

	public UserListDTO(String loginId, String fullName, String external, String userType) {
		this.loginId = loginId;
		this.fullName = fullName;
		this.external = external;
		this.userType = userType;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getExternal() {
		return external;
	}

	public void setExternal(String external) {
		this.external = external;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
}
