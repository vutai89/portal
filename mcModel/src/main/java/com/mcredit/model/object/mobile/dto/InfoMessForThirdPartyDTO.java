package com.mcredit.model.object.mobile.dto;

import java.sql.Clob;

public class InfoMessForThirdPartyDTO {
	public Long messId;
	private Clob appStatus;
	public Long externalUserMapping;
	public Long appNumber;
	public String appId;
	public String api;

	public Long getMessId() {
		return messId;
	}

	public void setMessId(Long messId) {
		this.messId = messId;
	}

	public Long getExternalUserMapping() {
		return externalUserMapping;
	}

	public void setExternalUserMapping(Long externalUserMapping) {
		this.externalUserMapping = externalUserMapping;
	}

	public Clob getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(Clob appStatus) {
		this.appStatus = appStatus;
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

	public InfoMessForThirdPartyDTO() {
		super();
	}

	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
	}

	public InfoMessForThirdPartyDTO(Long messId, Clob appStatus, Long externalUserMapping, Long appNumber, String appId,
			String api) {
		super();
		this.messId = messId;
		this.appStatus = appStatus;
		this.externalUserMapping = externalUserMapping;
		this.appNumber = appNumber;
		this.appId = appId;
		this.api = api;
	}

}
