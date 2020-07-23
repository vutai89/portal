package com.mcredit.data.mobile.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="USERS_PROFILES")
public class UsersProfiles implements Serializable {
	
	private static final long serialVersionUID = 6621985890166777721L;
	
	@Id
	@Column(name = "USER_ID")
	private Long userId;
	
	@Column(name = "MOBILE_IMEI")
	private String mobileImei;
	
	@Column(name = "OS_TYPE")
	private String osType;
	
	@Column(name = "NOTIFICATION_ID")
	private String notificationId;
	
	@Column(name = "EXTERNAL_USER_MAPPING_ID")
	private Long externalUserMappingId;
	
	@Column(name = "CONTACT_CENTER_ID")
	private String contactCenterId;
	
	@Column(name = "DEVICE_NAME")
	private String deviceName;

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getMobileImei() {
		return mobileImei;
	}

	public void setMobileImei(String mobileImei) {
		this.mobileImei = mobileImei;
	}

	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}

	public String getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}

	public Long getExternalUserMappingId() {
		return externalUserMappingId;
	}

	public void setExternalUserMappingId(Long externalUserMappingId) {
		this.externalUserMappingId = externalUserMappingId;
	}

	public String getContactCenterId() {
		return contactCenterId;
	}

	public void setContactCenterId(String contactCenterId) {
		this.contactCenterId = contactCenterId;
	}
	
}
