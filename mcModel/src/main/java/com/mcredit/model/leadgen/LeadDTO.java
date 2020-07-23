package com.mcredit.model.leadgen;

import java.io.Serializable;

import com.mcredit.model.object.RuleResult;

public class LeadDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3729670604998398799L;

	private String requestId;
	private String phoneNumber;
	private String telcoCode;
	private String scoreRange;
	private String nationalId;
	private String source;
	private String fullName;
	private String province;
	private String incomeLevel;
	private String other;
	private String refId;
	private String productCode;
	private String dob;
	private String partner;

	private long minScore;
	private long maxScore;
	private RuleResult ruleResult;
	private String campaignCode;
	
	private String gender;
	private String address;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getRefId() {
		return refId != null ? refId.trim() : "";
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getPhoneNumber() {
		return phoneNumber != null ? phoneNumber.trim() : "";
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getTelcoCode() {
		return telcoCode != null ? telcoCode.trim() : "";
	}

	public void setTelcoCode(String telcoCode) {
		this.telcoCode = telcoCode;
	}

	public String getScoreRange() {
		return scoreRange != null ? scoreRange.trim() : "";
	}

	public void setScoreRange(String scoreRange) {
		this.scoreRange = scoreRange;
	}

	public String getNationalId() {
		return nationalId != null ? nationalId.trim() : "";
	}

	public void setNationalId(String nationalId) {
		this.nationalId = nationalId;
	}

	public String getSource() {
		return source != null ? source.trim() : "";
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getFullName() {
		return fullName != null ? fullName.trim() : "";
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getProvince() {
		return province != null ? province.trim() : "";
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getIncomeLevel() {
		return incomeLevel != null ? incomeLevel.trim() : "";
	}

	public void setIncomeLevel(String incomeLevel) {
		this.incomeLevel = incomeLevel;
	}

	public String getOther() {
		return other != null ? other.trim() : "";
	}

	public void setOther(String other) {
		this.other = other;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public long getMinScore() {
		return minScore;
	}

	public void setMinScore(long minScore) {
		this.minScore = minScore;
	}

	public long getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(long maxScore) {
		this.maxScore = maxScore;
	}

	public RuleResult getRuleResult() {
		return ruleResult;
	}

	public void setRuleResult(RuleResult ruleResult) {
		this.ruleResult = ruleResult;
	}

	public String getCampaignCode() {
		return campaignCode;
	}

	public void setCampaignCode(String campaignCode) {
		this.campaignCode = campaignCode;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}