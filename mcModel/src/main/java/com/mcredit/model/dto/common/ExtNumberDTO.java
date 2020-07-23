package com.mcredit.model.dto.common;


public class ExtNumberDTO {
	
	private static final Long serialVersionUID = 1L;
	
	private String extNumber;
	private String deviceName;
	
	public ExtNumberDTO() {}
	
	public ExtNumberDTO(String extNumber) {
		this.extNumber = extNumber;		
	}
	
	public ExtNumberDTO(String extNumber, String deviceName) {
		this.extNumber = extNumber;		
		this.deviceName = deviceName;
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

	public static Long getSerialversionuid() {
		return serialVersionUID;
	}
}
