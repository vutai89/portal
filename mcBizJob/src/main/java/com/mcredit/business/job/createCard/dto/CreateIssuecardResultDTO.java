package com.mcredit.business.job.createCard.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateIssuecardResultDTO {
	@SerializedName("error_status")
	@Expose
	private String errorStatus;
	@SerializedName("issueId")
	@Expose
	private String issueId;
	@SerializedName("returnCode")
	@Expose
	private String returnCode;
	@SerializedName("returnMes")
	@Expose
	private String returnMes; 

	public String getErrorStatus() {
		return errorStatus;
	}

	public void setErrorStatus(String errorStatus) {
		this.errorStatus = errorStatus;
	}

	public String getIssueId() {
		return issueId;
	}

	public void setIssueId(String issueId) {
		this.issueId = issueId;
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
	
	public String toJson(){
		return "{" + (errorStatus != null ? "\"errorStatus\":\"" + errorStatus  : "")
				+ (issueId != null ? "\", \"issueId\":\"" + issueId : "")
				+ (returnCode != null ? "\",\"returnCode\":\"" + returnCode  : "")
				+ (returnMes != null ? "\",\"returnMes\":\"" + returnMes : "") + "\"}";
	}
}
