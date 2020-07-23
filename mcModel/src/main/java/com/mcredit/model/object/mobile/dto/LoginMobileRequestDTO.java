package com.mcredit.model.object.mobile.dto;

public class LoginMobileRequestDTO {

	private String username;
	private String password;
	private String notificationId;
	private String imei;
	private String osType;
	
	public String getOsType() {
		return osType;
	}

	public void setOsType(String iosType) {
		this.osType = iosType;
	}

	public String getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
