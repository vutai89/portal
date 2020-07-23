package com.mcredit.business.telesales.payload;

public class ScoringPayload {
	private Long idUplCustomer;
	private String primaryPhone;
	private String nationalId;
	private String verificationCode;
	private Long version;
	private String apiRequestId;
	private String refId;
	private String appNumber;
	private String nationalIdOld;
	
	// Contructor
	public ScoringPayload() {};
	public ScoringPayload(String primaryPhone, String nationalId, String verificationCode, String apiRequestId){
		this.primaryPhone = primaryPhone;
		this.nationalId = nationalId;
		this.verificationCode = verificationCode;
		this.apiRequestId = apiRequestId;
		this.version =new Long(2);
	}
	public Long getIdUplCustomer() {
		return idUplCustomer;
	}
	public void setIdUplCustomer(Long idUplCustomer) {
		this.idUplCustomer = idUplCustomer;
	}
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
	public String getVerificationCode() {
		return verificationCode;
	}
	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}
	public String getApiRequestId() {
		return apiRequestId;
	}
	public void setApiRequestId(String apiRequestId) {
		this.apiRequestId = apiRequestId;
	}
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public String getAppNumber() {
		return appNumber;
	}
	public void setAppNumber(String appNumber) {
		this.appNumber = appNumber;
	}
	public String getNationalIdOld() {
		return nationalIdOld;
	}
	public void setNationalIdOld(String nationalIdOld) {
		this.nationalIdOld = nationalIdOld;
	}
	
}
