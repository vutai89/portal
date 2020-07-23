package com.mcredit.business.telesales.object;

public class BPMScoringObject {
	
	private String vendorCode;
	private Integer status;
	private String score;
	private String dateTime;
	
	private String telcoCode;
	private String verifyInfo;
	
	public BPMScoringObject(String vendorCode, String score, Integer status, String dateTime) {
		this.vendorCode = vendorCode;
		this.score = score;
		this.status = status;
		this.dateTime = dateTime;
	}
	
	public String getVendorCode() {
		return vendorCode;
	}
	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
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
	public String getVerifyInfo() {
		return verifyInfo;
	}
	public void setVerifyInfo(String verifyInfo) {
		this.verifyInfo = verifyInfo;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
}
