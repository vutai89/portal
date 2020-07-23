package com.mcredit.model.leadgen;

import java.io.Serializable;

public class LeadResultDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5386212154455571494L;

	private String status;
	private String reason;
	private String bankLeadId;
	private String dateTime;

	public LeadResultDTO() {
	}

	public LeadResultDTO(String status, String reason, String bankLeadId, String dateTime) {
		this.status = status;
		this.reason = reason;
		this.bankLeadId = bankLeadId;
		this.dateTime = dateTime;
	}

	public String getBankLeadId() {
		return (bankLeadId!=null?bankLeadId:"");
	}

	public void setBankLeadId(String bankLeadId) {
		this.bankLeadId = bankLeadId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return (reason!=null?reason:"");
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
}