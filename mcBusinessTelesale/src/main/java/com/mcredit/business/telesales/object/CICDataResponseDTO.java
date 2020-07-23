package com.mcredit.business.telesales.object;

public class CICDataResponseDTO {
	
	private String returnCode;
	private String returnMes;
	private String id;
	private String otp;
	
	private String credit_score;
	private String client_count_30;
	
	// For message log
	private Long messageLogId;
	
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public String getCredit_score() {
		return credit_score;
	}
	public void setCredit_score(String credit_score) {
		this.credit_score = credit_score;
	}
	public String getClient_count_30() {
		return client_count_30;
	}
	public void setClient_count_30(String client_count_30) {
		this.client_count_30 = client_count_30;
	}
	public Long getMessageLogId() {
		return messageLogId;
	}
	public void setMessageLogId(Long messageLogId) {
		this.messageLogId = messageLogId;
	}
	
}
