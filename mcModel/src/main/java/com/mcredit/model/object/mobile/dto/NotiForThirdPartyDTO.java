package com.mcredit.model.object.mobile.dto;

public class NotiForThirdPartyDTO {
	private Long id;
	
	private String currentStatus;
	public Long appNumber;
	public String appId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
	

	public Long getAppNumber() {
		return appNumber;
	}

	public void setAppNumber(Long appNumber) {
		this.appNumber = appNumber;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public NotiForThirdPartyDTO(Long id, String currentStatus, Long appNumber, String appId) {
		super();
		this.id = id;
		this.currentStatus = currentStatus;
		this.appNumber = appNumber;
		this.appId = appId;
	}

	public NotiForThirdPartyDTO() {
		super();
	}
	
	
}
