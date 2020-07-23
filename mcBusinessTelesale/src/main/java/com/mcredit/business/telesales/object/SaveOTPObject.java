package com.mcredit.business.telesales.object;

public class SaveOTPObject {
	
	private String responseCode;
	private String vendorCode;
	private String requestID;
	private String telcoCode;
	
	public SaveOTPObject(String responseCode, String vendorCode, String requestID, String telcoCode) {
		super();
		this.responseCode = responseCode;
		this.vendorCode = vendorCode;
		this.requestID = requestID;
		this.telcoCode = telcoCode;
	}
	
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getVendorCode() {
		return vendorCode;
	}
	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}
	public String getRequestID() {
		return requestID;
	}
	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}
	public String getTelcoCode() {
		return telcoCode;
	}
	public void setTelcoCode(String telcoCode) {
		this.telcoCode = telcoCode;
	}
	

}
