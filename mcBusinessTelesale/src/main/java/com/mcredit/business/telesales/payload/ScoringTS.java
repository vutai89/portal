package com.mcredit.business.telesales.payload;

import java.util.Date;

public class ScoringTS {
	private Long id;
	private String customerName;
	private String customerPhone;
	private String customerIndentify;
	private String campaign;
	private String tsaCode;
	private String tsaName;
	private String isMark;
	private String requestDate;
	private String statusOTP;
	private String resultScoring;
	private Long minScore;
	private String verifyInfo;
	private Date dateMarkTs;
	private String dateMarkTsStr;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerPhone() {
		return customerPhone;
	}
	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}
	public String getCustomerIndentify() {
		return customerIndentify;
	}
	public void setCustomerIndentify(String customerIndentify) {
		this.customerIndentify = customerIndentify;
	}
	public String getCampaign() {
		return campaign;
	}
	public void setCampaign(String campaign) {
		this.campaign = campaign;
	}
	public String getTsaCode() {
		return tsaCode;
	}
	public void setTsaCode(String tsaCode) {
		this.tsaCode = tsaCode;
	}
	public String getTsaName() {
		return tsaName;
	}
	public void setTsaName(String tsaName) {
		this.tsaName = tsaName;
	}
	public String getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}
	public String getStatusOTP() {
		return statusOTP;
	}
	public void setStatusOTP(String statusOTP) {
		this.statusOTP = statusOTP;
	}
	public String getResultScoring() {
		return resultScoring;
	}
	public void setResultScoring(String resultScoring) {
		this.resultScoring = resultScoring;
	}
	public Long getMinScore() {
		return minScore;
	}
	public void setMinScore(Long minScore) {
		this.minScore = minScore;
	}
	public String getVerifyInfo() {
		return verifyInfo;
	}
	public void setVerifyInfo(String verifyInfo) {
		this.verifyInfo = verifyInfo;
	}
	public String getIsMark() {
		return isMark;
	}
	public void setIsMark(String isMark) {
		this.isMark = isMark;
	}
	public Date getDateMarkTs() {
		return dateMarkTs;
	}
	public void setDateMarkTs(Date dateMarkTs) {
		this.dateMarkTs = dateMarkTs;
	}
	public String getDateMarkTsStr() {
		return dateMarkTsStr;
	}
	public void setDateMarkTsStr(String dateMarkTsStr) {
		this.dateMarkTsStr = dateMarkTsStr;
	}
	
}
