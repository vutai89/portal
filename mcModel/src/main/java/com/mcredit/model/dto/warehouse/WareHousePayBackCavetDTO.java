/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcredit.model.dto.warehouse;

import java.util.Date;

public class WareHousePayBackCavetDTO {

    public String contractNumber;
    public String statusAppPayBack;
    public String appPayBackDateTo;
    public String appPayBackDateFrom;
    public String chassis;
    public Integer caseNum;
    public String engine;
    public String statusWhCode;
    public String flow;
    public String fwAppPayBackDateTo;
    public String fwAppPayBackDateFrom;
    public int typeScreen;

    public WareHousePayBackCavetDTO(String contractNumber, String statusAppPayBack, String appPayBackDateTo, String appPayBackDateFrom, String chassis, String engine, Integer caseNum, String statusWhCode, String flow, String fwAppPayBackDateTo, String fwAppPayBackDateFrom, int typeScreen) {
        this.contractNumber = contractNumber;
        this.statusAppPayBack = statusAppPayBack;
        this.appPayBackDateTo = appPayBackDateTo;
        this.appPayBackDateFrom = appPayBackDateFrom;
        this.chassis = chassis;
        this.engine = engine;
        this.caseNum = caseNum;
        this.statusWhCode = statusWhCode;
        this.flow = flow;
        this.fwAppPayBackDateTo = fwAppPayBackDateTo;
        this.fwAppPayBackDateFrom = fwAppPayBackDateFrom;
        this.typeScreen = typeScreen;
    }

    public String getAppPayBackDateTo() {
        return appPayBackDateTo;
    }

    public void setAppPayBackDateTo(String appPayBackDateTo) {
        this.appPayBackDateTo = appPayBackDateTo;
    }

    public String getAppPayBackDateFrom() {
        return appPayBackDateFrom;
    }

    public void setAppPayBackDateFrom(String appPayBackDateFrom) {
        this.appPayBackDateFrom = appPayBackDateFrom;
    }

    public String getFwAppPayBackDateTo() {
        return fwAppPayBackDateTo;
    }

    public void setFwAppPayBackDateTo(String fwAppPayBackDateTo) {
        this.fwAppPayBackDateTo = fwAppPayBackDateTo;
    }

    public String getFwAppPayBackDateFrom() {
        return fwAppPayBackDateFrom;
    }

    public void setFwAppPayBackDateFrom(String fwAppPayBackDateFrom) {
        this.fwAppPayBackDateFrom = fwAppPayBackDateFrom;
    }

    public int getTypeScreen() {
        return typeScreen;
    }

    public void setTypeScreen(int typeScreen) {
        this.typeScreen = typeScreen;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getStatusAppPayBack() {
        return statusAppPayBack;
    }

    public void setStatusAppPayBack(String statusAppPayBack) {
        this.statusAppPayBack = statusAppPayBack;
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

 
    public String getStatusWhCode() {
        return statusWhCode;
    }

    public void setStatusWhCode(String statusWhCode) {
        this.statusWhCode = statusWhCode;
    }

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }

	public Integer getCaseNum() {
		return caseNum;
	}

	public void setCaseNum(Integer caseNum) {
		this.caseNum = caseNum;
	}  

}
