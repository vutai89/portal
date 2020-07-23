package com.mcredit.model.dto;

import java.util.Date;

public class CustomerDTO {
	public Long id;
	public String linkValue;
	public String custName;
	public String identityNumber;
	public Date identityIssueDate;
	public String codeValue1;
	public String mobile;
	public String coreCustCode;

	public String getLinkValue() {
		return linkValue;
	}

	public void setLinkValue(String linkValue) {
		this.linkValue = linkValue;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getIdentityNumber() {
		return identityNumber;
	}

	public void setIdentityNumber(String identityNumber) {
		this.identityNumber = identityNumber;
	}

	public Date getIdentityIssueDate() {
		return identityIssueDate;
	}

	public void setIdentityIssueDate(Date identityIssueDate) {
		this.identityIssueDate = identityIssueDate;
	}

	public String getCodeValue1() {
		return codeValue1;
	}

	public void setCodeValue1(String codeValue1) {
		this.codeValue1 = codeValue1;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCoreCustCode() {
		return coreCustCode;
	}

	public void setCoreCustCode(String coreCustCode) {
		this.coreCustCode = coreCustCode;
	}

}
