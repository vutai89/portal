package com.mcredit.model.dto.debt_home;

public class DebtContractInfo {

	private String appNumber;
	private String contractNumber;
	private String identityNumber;
	private String mobilePhone;
	private String customerName;
	private String oldIdentityNumber;

	public String getAppNumber() {
		return appNumber;
	}

	public void setAppNumber(String appNumber) {
		this.appNumber = appNumber;
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public String getIdentityNumber() {
		return identityNumber;
	}

	public void setIdentityNumber(String identityNumber) {
		this.identityNumber = identityNumber;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public String getOldIdentityNumber() {
		return oldIdentityNumber;
	}

	public void setOldIdentityNumber(String oldIdentityNumber) {
		this.oldIdentityNumber = oldIdentityNumber;
	}

	public DebtContractInfo(String appNumber, String contractNumber, String identityNumber, String mobilePhone, String customerName, String oldIdentityNumber) {
		super();
		this.appNumber = appNumber;
		this.contractNumber = contractNumber;
		this.identityNumber = identityNumber;
		this.mobilePhone = mobilePhone;
		this.customerName = customerName;
		this.oldIdentityNumber = oldIdentityNumber;
	}

	public DebtContractInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

}
