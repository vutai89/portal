package com.mcredit.model.dto.appraisal;

public class AppraisalObjectDTO {
	
	private String action;				// actor 'C' (Call) or 'A' (Approve) or 'B' (BPM data)
	private String updateUser;
	private String bpmAppId;
	private String conclude;			// ket qua tham dinh '1' (success) or '0' (fail) or 'null' not appraisal
	private String transactionId;
	private String token;
	private AppraisalDataDetailDTO appraisalDataDetail;
	
	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public String getUpdateUser() {
		return updateUser;
	}
	
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getBpmAppId() {
		return bpmAppId;
	}

	public void setBpmAppId(String bpmAppId) {
		this.bpmAppId = bpmAppId;
	}

	public String getConclude() {
		return conclude;
	}
	
	public void setConclude(String conclude) {
		this.conclude = conclude;
	}
	
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public AppraisalDataDetailDTO getAppraisalDataDetail() {
		return appraisalDataDetail;
	}
	
	public void setAppraisalDataDetail(AppraisalDataDetailDTO appraisalDataDetail) {
		this.appraisalDataDetail = appraisalDataDetail;
	}
	
	
}
