package com.mcredit.model.object.audit;

public class AuditMatchDetail {
	private String partnerRefId;
	private String contractNumber;
	private String paymentAmount;
	private String date;
	private String status;

	public String getPartnerRefId() {
		return partnerRefId;
	}

	public void setPartnerRefId(String partnerRefId) {
		this.partnerRefId = partnerRefId;
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public String getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public AuditMatchDetail(String partnerRefId, String contractNumber, String paymentAmount, String date,
			String status) {
		super();
		this.partnerRefId = partnerRefId;
		this.contractNumber = contractNumber;
		this.paymentAmount = paymentAmount;
		this.date = date;
		this.status = status;
	}

	public AuditMatchDetail() {
		super();
	}

}
