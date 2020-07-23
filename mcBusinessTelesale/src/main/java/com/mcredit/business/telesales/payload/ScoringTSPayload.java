package com.mcredit.business.telesales.payload;

public class ScoringTSPayload {

	private String phoneNumber;
	private String identityNumber;
	
	public ScoringTSPayload(String phoneNumber, String identityNumber) {
		this.phoneNumber = phoneNumber;
		this.identityNumber = identityNumber;
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
