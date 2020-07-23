package com.mcredit.model.leadgen;

import java.io.Serializable;

public class SendLeadInput implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3729670604998398799L;

	private String phoneNumber;
	private String telcoCode;
	private String scoreRange;
	private String nationalId;
	private String source;
	private String fullName;
	private String province;
	private String incomeLevel;
	private String other;
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getTelcoCode() {
		return telcoCode;
	}
	public void setTelcoCode(String telcoCode) {
		this.telcoCode = telcoCode;
	}
	public String getScoreRange() {
		return scoreRange;
	}
	public void setScoreRange(String scoreRange) {
		this.scoreRange = scoreRange;
	}
	public String getNationalId() {
		return nationalId;
	}
	public void setNationalId(String nationalId) {
		this.nationalId = nationalId;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getIncomeLevel() {
		return incomeLevel;
	}
	public void setIncomeLevel(String incomeLevel) {
		this.incomeLevel = incomeLevel;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
}