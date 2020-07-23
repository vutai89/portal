package com.mcredit.model.object.audit;

public class DisAudit {
	private Long paymentAmount; // so tien giao dich
	private String channelCode; // kenh doi tac
	private String partnerRefID; // ma systemtrace
	private String contractNumber; // ma ho so
	private String date; // ngay giao dich
	private String status; // trang thai giao dich
	private String customerName;
	private String customerId;
	private String type; // giao dich thu/chi
	private String timeControl; // gio doi soat
	private String workFlow; // loai thanh toan
	private String result; // ket qua doi soat
	
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
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(Long paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	public String getPartnerRefID() {
		return partnerRefID;
	}
	public void setPartnerRefID(String partnerRefID) {
		this.partnerRefID = partnerRefID;
	}
	public String getContractNumber() {
		return contractNumber;
	}
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
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
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
}
