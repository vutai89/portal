package com.mcredit.business.job.sms.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendSMSResultDTO {
	@SerializedName("SendSMSResultCollection")
	@Expose
	private SendSMSResultCollection sendSMSResultCollection;
	
	@SerializedName("returnCode")
	@Expose
	private String returnCode;

	@SerializedName("returnMes")
	@Expose
	private String returnMes;

	public SendSMSResultCollection getSendSMSResultCollection() {
		return sendSMSResultCollection;
	}

	public void setSendSMSResultCollection(SendSMSResultCollection sendSMSResultCollection) {
		this.sendSMSResultCollection = sendSMSResultCollection;
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
}
