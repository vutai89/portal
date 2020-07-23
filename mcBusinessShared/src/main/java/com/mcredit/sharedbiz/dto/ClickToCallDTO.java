package com.mcredit.sharedbiz.dto;


public class ClickToCallDTO {
	
	private static final Long serialVersionUID = 1L;
	
	private String agentID;
	private String extNumber;
	private String deviceName;
	private String mobilePhone;
	
	public ClickToCallDTO() {}
	
	public ClickToCallDTO(String agentID, String extNumber, String deviceName, String mobilePhone) {
		this.agentID = agentID;
		this.extNumber = extNumber;
		this.deviceName = deviceName;
		this.mobilePhone = mobilePhone;
	}
	
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getExtNumber() {
		return extNumber;
	}
	public void setExtNumber(String extNumber) {
		this.extNumber = extNumber;
	}
	public String getAgentID() {
		return agentID;
	}
	public void setAgentID(String agentID) {
		this.agentID = agentID;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public static Long getSerialversionuid() {
		return serialVersionUID;
	}
}
