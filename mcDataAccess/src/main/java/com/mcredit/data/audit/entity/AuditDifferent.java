package com.mcredit.data.audit.entity;

public class AuditDifferent {
	private String contractNumber;
	private String customerName;
	private String cmnd;
	private Long money;
	private String date;
	private String ref;
	public String getContractNumber() {
		return contractNumber;
	}
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCmnd() {
		return cmnd;
	}
	public void setCmnd(String cmnd) {
		this.cmnd = cmnd;
	}
	public Long getMoney() {
		return money;
	}
	public void setMoney(Long money) {
		this.money = money;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}

	public AuditDifferent(String contractNumber, String customerName, String cmnd, Long money, String date,
			String ref) {
		super();
		this.contractNumber = contractNumber;
		this.customerName = customerName;
		this.cmnd = cmnd;
		this.money = money;
		this.date = date;
		this.ref = ref;
	}
	public AuditDifferent() {
		super();
	}
	
	
}
