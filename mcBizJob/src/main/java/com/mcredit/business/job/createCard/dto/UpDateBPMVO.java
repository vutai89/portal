package com.mcredit.business.job.createCard.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpDateBPMVO  {

	@SerializedName("coreCustCode")
	@Expose
	private String coreCustCode;
	@SerializedName("coreAccountNumber")
	@Expose
	private String coreAccountNumber;
	@SerializedName("cardIdNumber")
	@Expose
	private String cardIdNumber;
	@SerializedName("cardNumber")
	@Expose
	private String cardNumber;
	@SerializedName("coreAccountNumber2")
	@Expose
	private String coreAccountNumber2;
	@SerializedName("issueId")
	@Expose
	private String issueId;

	public String getCoreCustCode() {
		return coreCustCode;
	}

	public void setCoreCustCode(String coreCustCode) {
		this.coreCustCode = coreCustCode;
	}

	public String getCoreAccountNumber() {
		return coreAccountNumber;
	}

	public void setCoreAccountNumber(String coreAccountNumber) {
		this.coreAccountNumber = coreAccountNumber;
	}

	public String getCardIdNumber() {
		return cardIdNumber;
	}

	public void setCardIdNumber(String cardIdNumber) {
		this.cardIdNumber = cardIdNumber;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCoreAccountNumber2() {
		return coreAccountNumber2;
	}

	public void setCoreAccountNumber2(String coreAccountNumber2) {
		this.coreAccountNumber2 = coreAccountNumber2;
	}

	public String getIssueId() {
		return issueId;
	}

	public void setIssueId(String issueId) {
		this.issueId = issueId;
	}
	
	public String toJson(){
		return "{" + (coreCustCode != null ? "\"coreCustCode\":\"" + coreCustCode  : "")
				+ (coreAccountNumber != null ? "\", \"coreAccountNumber\":\"" + coreAccountNumber : "")
				+ (cardIdNumber != null ? "\",\"cardIdNumber\":\"" + cardIdNumber  : "")
				+ (cardNumber != null ? "\", \"cardNumber\":\"" + cardNumber : "")
				+ (coreAccountNumber2 != null ? "\",\"coreAccountNumber2\":\"" + coreAccountNumber2 : "")
				+ (issueId != null ? "\",\"issueId\":\"" + issueId : "") + "\"}";
	}

	
}
