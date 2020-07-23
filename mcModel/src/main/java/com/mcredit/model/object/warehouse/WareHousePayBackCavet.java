package com.mcredit.model.object.warehouse;

import java.util.List;

public class WareHousePayBackCavet {

    public Long idWhDocument;
    public String contractNumber;
    public String docTypeCode;
    public String docTypeName;
    public String custName;
    public String flow;
    public String postCode;
    public String saleName;
    public String saleMobile;
    public String emailSale;
    public String emailBDS;
    public String commodity;
    public String brand;
    public String modelCode;
    public String color;
    public String chassis;
    public String engine;
    public Integer caseNum;
    public String nPlate;
    public String cavetNumber;
    public String statusWhName;
    public String statusWhCode;
    public String createdBySaveWH;
    public String createdDateSaveWH;
    public String borrowedUser;
    public String whCode;

    public String statusAppPayBack;
    public String appPayBackDate;
    public String rejectPayBack;
    public String payBackOrFwAppPayBackDate;

    public List<WhLstErrPayBackCavet> lstErrPayBackCavets;

    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

    public String getPayBackOrFwAppPayBackDate() {
        return payBackOrFwAppPayBackDate;
    }

    public void setPayBackOrFwAppPayBackDate(String payBackOrFwAppPayBackDate) {
        this.payBackOrFwAppPayBackDate = payBackOrFwAppPayBackDate;
    }

    public String getBorrowedUser() {
        return borrowedUser;
    }

    public void setBorrowedUser(String borrowedUser) {
        this.borrowedUser = borrowedUser;
    }

    public String getStatusAppPayBack() {
        return statusAppPayBack;
    }

    public void setStatusAppPayBack(String statusAppPayBack) {
        this.statusAppPayBack = statusAppPayBack;
    }

    public String getAppPayBackDate() {
        return appPayBackDate;
    }

    public void setAppPayBackDate(String appPayBackDate) {
        this.appPayBackDate = appPayBackDate;
    }

    public String getRejectPayBack() {
        return rejectPayBack;
    }

    public void setRejectPayBack(String rejectPayBack) {
        this.rejectPayBack = rejectPayBack;
    }

    public List<WhLstErrPayBackCavet> getLstErrPayBackCavets() {
        return lstErrPayBackCavets;
    }

    public void setLstErrPayBackCavets(List<WhLstErrPayBackCavet> lstErrPayBackCavets) {
        this.lstErrPayBackCavets = lstErrPayBackCavets;
    }

    public String getDocTypeCode() {
        return docTypeCode;
    }

    public void setDocTypeCode(String docTypeCode) {
        this.docTypeCode = docTypeCode;
    }

    public String getDocTypeName() {
        return docTypeName;
    }

    public void setDocTypeName(String docTypeName) {
        this.docTypeName = docTypeName;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getStatusWhName() {
        return statusWhName;
    }

    public void setStatusWhName(String statusWhName) {
        this.statusWhName = statusWhName;
    }

    public String getStatusWhCode() {
        return statusWhCode;
    }

    public void setStatusWhCode(String statusWhCode) {
        this.statusWhCode = statusWhCode;
    }

    public Long getIdWhDocument() {
        return idWhDocument;
    }

    public void setIdWhDocument(Long idWhDocument) {
        this.idWhDocument = idWhDocument;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }

    public String getSaleName() {
        return saleName;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName;
    }

    public String getSaleMobile() {
        return saleMobile;
    }

    public void setSaleMobile(String saleMobile) {
        this.saleMobile = saleMobile;
    }

    public String getEmailSale() {
        return emailSale;
    }

    public void setEmailSale(String emailSale) {
        this.emailSale = emailSale;
    }

    public String getEmailBDS() {
        return emailBDS;
    }

    public void setEmailBDS(String emailBDS) {
        this.emailBDS = emailBDS;
    }

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getChassis() {
        return chassis;
    }

    public void setChassis(String chassis) {
        this.chassis = chassis;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getnPlate() {
        return nPlate;
    }

    public void setnPlate(String nPlate) {
        this.nPlate = nPlate;
    }

    public String getCavetNumber() {
        return cavetNumber;
    }

    public void setCavetNumber(String cavetNumber) {
        this.cavetNumber = cavetNumber;
    }

    public String getCreatedDateSaveWH() {
        return createdDateSaveWH;
    }

    public void setCreatedDateSaveWH(String createdDateSaveWH) {
        this.createdDateSaveWH = createdDateSaveWH;
    }

    public String getCreatedBySaveWH() {
        return createdBySaveWH;
    }

    public void setCreatedBySaveWH(String createdBySaveWH) {
        this.createdBySaveWH = createdBySaveWH;
    }

	public Integer getCaseNum() {
		return caseNum;
	}

	public void setCaseNum(Integer caseNum) {
		this.caseNum = caseNum;
	}
}
