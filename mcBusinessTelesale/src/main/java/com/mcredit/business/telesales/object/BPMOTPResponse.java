package com.mcredit.business.telesales.object;

public class BPMOTPResponse {

	private String returnCode;
	private String returnMes;
	private String time;
	private String telcoCode;
	private String requestId;
	private Integer status;
	private String otp;
	
	public BPMOTPResponse(String returnCode, String returnMes) {
		this.returnCode = returnCode;
		this.returnMes = returnMes;
	}
	
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnMes() {
		return returnMes;
	}
	public void setReturnMes(String returnMes) {
		this.returnMes = returnMes;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getTelcoCode() {
		return telcoCode;
	}
	public void setTelcoCode(String telcoCode) {
		this.telcoCode = telcoCode;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
}
