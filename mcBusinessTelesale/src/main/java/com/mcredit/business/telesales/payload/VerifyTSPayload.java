package com.mcredit.business.telesales.payload;

public class VerifyTSPayload {
	
	private String otp;
	private String request_id;
	
	public VerifyTSPayload (String otp, String request_id) {
		this.otp = otp;
		this.request_id = request_id;
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
	
}
