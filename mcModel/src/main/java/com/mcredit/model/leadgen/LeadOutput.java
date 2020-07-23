package com.mcredit.model.leadgen;

import java.io.Serializable;

public class LeadOutput implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -5386212154455571494L;
	
	private String status;
	private String reason;
	private String dateTime;
	
	public LeadOutput() {
	}
	
	public LeadOutput(String status, String reason, String dateTime) {
		this.status = status;
		this.reason = reason;
		this.dateTime = dateTime;
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