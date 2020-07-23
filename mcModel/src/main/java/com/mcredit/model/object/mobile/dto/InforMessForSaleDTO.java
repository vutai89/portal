package com.mcredit.model.object.mobile.dto;

import java.sql.Clob;

public class InforMessForSaleDTO {
	private Long messId;
	private String notificationId;
	private Clob appStatus;
	private Long documentId;
	private String citizenId;
	private String customerName;
	private String messStatus;
	private String osType;
	
	public Long getDocumentId() {
		return documentId;
	}
	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}
	public String getCitizenId() {
		return citizenId;
	}
	public void setCitizenId(String citizenId) {
		this.citizenId = citizenId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Long getMessId() {
		return messId;
	}
	public void setMessId(Long messId) {
		this.messId = messId;
	}
	public String getNotificationId() {
		return notificationId;
	}
	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}
	public Clob getAppStatus() {
		return appStatus;
	}
	public void setAppStatus(Clob appStatus) {
		this.appStatus = appStatus;
	}
	public String getMessStatus() {
		return messStatus;
	}
	public void setMessStatus(String messStatus) {
		this.messStatus = messStatus;
	}
	public String getOsType() {
		return osType;
	}
	public void setOsType(String osType) {
		this.osType = osType;
	}
	
}
