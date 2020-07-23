package com.mcredit.business.telesales.payload;

public class ScoringDTO {
	
	private String primaryPhone;
	private String nationalId;
	private String nationalIdOld;
	private String appNumber;
	
	private String verificationCode;
	private String vendorCode;
	private String requestId;
	
	public String getPrimaryPhone() {
		return primaryPhone;
	}
	public void setPrimaryPhone(String primaryPhone) {
		this.primaryPhone = primaryPhone;
	}
	public String getNationalId() {
		return nationalId;
	}
	public void setNationalId(String nationalId) {
		this.nationalId = nationalId;
	}
	public String getAppNumber() {
		return appNumber;
	}
	public void setAppNumber(String appNumber) {
		this.appNumber = appNumber;
	}
	public String getVerificationCode() {
		return verificationCode;
	}
	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}
	public String getVendorCode() {
		return vendorCode;
	}
	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getNationalIdOld() {
		return nationalIdOld;
	}
	public void setNationalIdOld(String nationalIdOld) {
		this.nationalIdOld = nationalIdOld;
	}
	
}
