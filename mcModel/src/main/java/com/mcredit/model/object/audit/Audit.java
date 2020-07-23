package com.mcredit.model.object.audit;

public class Audit{	
	private String paymentAmount; // so tien giao dich = fee
	private String paymentChannelCode; // kenh giao dich = third_party
	private String partnerRefId; // = partnerRefId
	private String contractId; // so contract number/ so id the khach hang
	private String auditDate; // ngay thuc hien giao dich
	private String status; // trang thai giao dich - thanh cong/huy
	private String type; // giao dich thu/chi
	private String timeControl; // thoi gian giao dich
	private String workFlow; // loai thanh toan : 0 khoan vay, 1 the tin dung.
	public String getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public String getPaymentChannelCode() {
		return paymentChannelCode;
	}
	public void setPaymentChannelCode(String paymentChannelCode) {
		this.paymentChannelCode = paymentChannelCode;
	}
	public String getPartnerRefId() {
		return partnerRefId;
	}
	public void setPartnerRefId(String partnerRefId) {
		this.partnerRefId = partnerRefId;
	}
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public String getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(String auditDate) {
		this.auditDate = auditDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTimeControl() {
		return timeControl;
	}
	public void setTimeControl(String timeControl) {
		this.timeControl = timeControl;
	}
	public String getWorkFlow() {
		return workFlow;
	}
	public void setWorkFlow(String workFlow) {
		this.workFlow = workFlow;
	}
	public Audit() {
		super();
	}
	public Audit(String paymentAmount, String paymentChannelCode, String partnerRefId, String contractId,
			String auditDate, String status, String type, String timeControl, String workFlow) {
		super();
		this.paymentAmount = paymentAmount;
		this.paymentChannelCode = paymentChannelCode;
		this.partnerRefId = partnerRefId;
		this.contractId = contractId;
		this.auditDate = auditDate;
		this.status = status;
		this.type = type;
		this.timeControl = timeControl;
		this.workFlow = workFlow;
	}

	
}
