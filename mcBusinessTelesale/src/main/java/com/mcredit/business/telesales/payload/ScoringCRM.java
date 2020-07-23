package com.mcredit.business.telesales.payload;

public class ScoringCRM {
	
	private String uplCustomerId;
	private String otp;
	private String request_id;
	private String phoneNumber;
	private String identityNumber;
	
	public String getUplCustomerId() {
		return uplCustomerId;
	}
	public void setUplCustomerId(String uplCustomerId) {
		this.uplCustomerId = uplCustomerId;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public String getRequest_id() {
		return request_id;
	}
	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getIdentityNumber() {
		return identityNumber;
	}
	public void setIdentityNumber(String identityNumber) {
		this.identityNumber = identityNumber;
	}
	
}
