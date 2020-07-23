package com.mcredit.model.dto.send_mail;

public class DataMailDTO {
	private String appId;
	private String appNumber;
	private String custName;
	private String contractNumber;
	private String contractDate;
	private String status;
	private String mailSale;
	private String mailLead;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppNumber() {
        return appNumber;
    }

    public void setAppNumber(String appNumber) {
        this.appNumber = appNumber;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMailSale() {
        return mailSale;
    }

    public void setMailSale(String mailSale) {
        this.mailSale = mailSale;
    }

    public String getMailLead() {
        return mailLead;
    }

    public void setMailLead(String mailLead) {
        this.mailLead = mailLead;
    }
}
