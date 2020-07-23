package com.mcredit.model.object.warehouse;


public class WareHousePayBackLetter {

    public String contractNumber;
    public Integer caseNum;
    public String contractDate;
    public String actualClosedDate;
    public String custName;
    public String identityNumber;
    public String identityIssueDate;
    public String identityIssuePlace;
    public String address;
    public String contactValue;

    public WareHousePayBackLetter(String contractNumber, String identityNumber, Integer caseNum) {
        this.contractNumber = contractNumber;
        this.identityNumber = identityNumber;
        this.caseNum = caseNum;
    }

    public WareHousePayBackLetter() {
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getContractDate() {
        return contractDate;
    }

    public void setContractDate(String contractDate) {
        this.contractDate = contractDate;
    }

    public String getActualClosedDate() {
        return actualClosedDate;
    }

    public void setActualClosedDate(String actualClosedDate) {
        this.actualClosedDate = actualClosedDate;
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

    public String getIdentityIssueDate() {
        return identityIssueDate;
    }

    public void setIdentityIssueDate(String identityIssueDate) {
        this.identityIssueDate = identityIssueDate;
    }

    public String getIdentityIssuePlace() {
        return identityIssuePlace;
    }

    public void setIdentityIssuePlace(String identityIssuePlace) {
        this.identityIssuePlace = identityIssuePlace;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactValue() {
        return contactValue;
    }

    public void setContactValue(String contactValue) {
        this.contactValue = contactValue;
    }

	public Integer getCaseNum() {
		return caseNum;
	}

	public void setCaseNum(Integer caseNum) {
		this.caseNum = caseNum;
	}
    
}
