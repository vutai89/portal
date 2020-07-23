package com.mcredit.business.job.sms.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemSendSMSResult {
	@Expose
	private Integer receiverID;
	@SerializedName("status")
	@Expose
	private Boolean status;
	@SerializedName("errorMessage")
	@Expose
	private String errorMessage;

	public Integer getReceiverID() {
		return receiverID;
	}

	public void setReceiverID(Integer receiverID) {
		this.receiverID = receiverID;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
