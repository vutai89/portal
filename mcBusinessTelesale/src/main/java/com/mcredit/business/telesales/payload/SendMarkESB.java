package com.mcredit.business.telesales.payload;

public class SendMarkESB {
	public String primaryPhone;
	public String nationalId;
	public String verificationCode;
	public Integer version;

	public SendMarkESB(String primaryPhone,String nationalId,String verificationCode,Integer version) {
		// TODO Auto-generated constructor stub
		this.primaryPhone=primaryPhone;
		this.nationalId=nationalId;
		this.verificationCode=verificationCode;
		this.version=version;
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

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	
	
	
}
