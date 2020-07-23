package com.mcredit.model.object.audit;

public class AuditUnMatchDetail {
	private String TPPartnerRefId;
	private String MCPartnerRefId;
	private String contractNumber;
	private Long TPPaymentAmount;
	private Long MCPaymentAmount;
	private String date;
	private String TPStatus;
	private String MCStatus;
	private String result;
	public String getTPPartnerRefId() {
		return TPPartnerRefId;
	}
	public void setTPPartnerRefId(String tPPartnerRefId) {
		TPPartnerRefId = tPPartnerRefId;
	}
	public String getMCPartnerRefId() {
		return MCPartnerRefId;
	}
	public void setMCPartnerRefId(String mCPartnerRefId) {
		MCPartnerRefId = mCPartnerRefId;
	}
	public String getContractNumber() {
		return contractNumber;
	}
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
	public Long getTPPaymentAmount() {
		return TPPaymentAmount;
	}
	public void setTPPaymentAmount(Long tPPaymentAmount) {
		TPPaymentAmount = tPPaymentAmount;
	}
	public Long getMCPaymentAmount() {
		return MCPaymentAmount;
	}
	public void setMCPaymentAmount(Long mCPaymentAmount) {
		MCPaymentAmount = mCPaymentAmount;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTPStatus() {
		return TPStatus;
	}
	public void setTPStatus(String tPStatus) {
		TPStatus = tPStatus;
	}
	public String getMCStatus() {
		return MCStatus;
	}
	public void setMCStatus(String mCStatus) {
		MCStatus = mCStatus;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public AuditUnMatchDetail(String tPPartnerRefId, String mCPartnerRefId, String contractNumber, Long tPPaymentAmount,
			Long mCPaymentAmount, String date, String tPStatus, String mCStatus, String result) {
		super();
		TPPartnerRefId = tPPartnerRefId;
		MCPartnerRefId = mCPartnerRefId;
		this.contractNumber = contractNumber;
		TPPaymentAmount = tPPaymentAmount;
		MCPaymentAmount = mCPaymentAmount;
		this.date = date;
		TPStatus = tPStatus;
		MCStatus = mCStatus;
		this.result = result;
	}
	public AuditUnMatchDetail() {
		super();
	}
}
