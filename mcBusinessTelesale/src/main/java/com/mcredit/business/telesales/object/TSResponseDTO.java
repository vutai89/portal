package com.mcredit.business.telesales.object;

public class TSResponseDTO {
	
	private String requestId;
	private String telcoCode;
	private String returnCode;
	private String returnMes;
	private String time;
	private String otp;
	
	// Scoring
	private String score;
	private String verify;
	private String telco_code;
	private String request_id;
	
	// For message_log
	private Long messageLogId;
	
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getVerify() {
		return verify;
	}
	public void setVerify(String verify) {
		this.verify = verify;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getTelcoCode() {
		return telcoCode;
	}
	public void setTelcoCode(String telcoCode) {
		this.telcoCode = telcoCode;
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
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTelco_code() {
		return telco_code;
	}
	public void setTelco_code(String telco_code) {
		this.telco_code = telco_code;
	}
	public String getRequest_id() {
		return request_id;
	}
	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public Long getMessageLogId() {
		return messageLogId;
	}
	public void setMessageLogId(Long messageLogId) {
		this.messageLogId = messageLogId;
	}
}
