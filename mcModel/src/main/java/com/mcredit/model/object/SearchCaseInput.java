package com.mcredit.model.object;

import java.io.Serializable;

public class SearchCaseInput implements Serializable {
	private static final long serialVersionUID = -5294390630539645841L;

	public Integer docTypeId;
	public String identityNum;
	public String contractNum;
	public String appNum;
	public String productGroupId;
	public String frameNum;
	public String serialNum;
	public String numPlate;
	public String receiveDateFrom;
	public String receiveDateTo;
	public String assignDateFrom;
	public String assignDateTo;
	public Integer assigneeId;
	public Integer assignType;
	public Integer statusProcess;
	public String storageStatus;
	public String processDateFrom;
	public String processDateTo;
	public Integer statusAllocate;

	public String lodgeDateFrom;
	public String lodgeDateTo;

	public String statusBorrow;
	public String borrowDateFrom;
	public String borrowDateTo;

	public String borrowApproveDateFrom;
	public String borrowApproveDateTo;

	public String toApproveDateFrom;
	public String toApproveDateTo;

	public String workFlow;
	public Long whdId;
	public String importer;
	public String statusAppPayBack;

	public String appDateFrom;
	public String appDateTo;

	public String appPayBackDateFrom;
	public String appPayBackDateTo;

	public SearchCaseInput(String contractNum) {
		super();
		this.contractNum = contractNum;
	}

	public SearchCaseInput(Integer docTypeId, String identityNum, String contractNum, String appNum, String frameNum,
			String serialNum, String numPlate, String receiveDateFrom, String receiveDateTo, String assignDateFrom,
			String assignDateTo, Integer assigneeId, Integer assignType, Integer statusProcess, String storageStatus,
			String processDateFrom, String processDateTo, Integer statusAllocate, String lodgeDateFrom,
			String lodgeDateTo, String borrowDateFrom, String borrowDateTo, String borrowApproveDateFrom,
			String borrowApproveDateTo, String toApproveDateFrom, String toApproveDateTo, String productGroupId,
			String workFlow, Long whdId, String statusBorrow, String importer, String statusAppPayBack,
			String appPayBackDateFrom, String appPayBackDateTo , String appDateFrom , String appDateTo ) {
		super();
		this.docTypeId = docTypeId;
		this.identityNum = identityNum;
		this.contractNum = contractNum;
		this.appNum = appNum;
		this.frameNum = frameNum;
		this.serialNum = serialNum;
		this.numPlate = numPlate;
		this.receiveDateFrom = receiveDateFrom;
		this.receiveDateTo = receiveDateTo;
		this.assignDateFrom = assignDateFrom;
		this.assignDateTo = assignDateTo;
		this.assigneeId = assigneeId;
		this.assignType = assignType;
		this.statusProcess = statusProcess;
		this.storageStatus = storageStatus;
		this.processDateFrom = processDateFrom;
		this.processDateTo = processDateTo;
		this.lodgeDateFrom = lodgeDateFrom;
		this.lodgeDateTo = lodgeDateTo;
		this.borrowDateFrom = borrowDateFrom;
		this.borrowDateTo = borrowDateTo;
		this.borrowApproveDateFrom = borrowApproveDateFrom;
		this.borrowApproveDateTo = borrowApproveDateTo;
		this.toApproveDateFrom = toApproveDateFrom;
		this.toApproveDateTo = toApproveDateTo;
		this.productGroupId = productGroupId;
		this.statusAllocate = statusAllocate;
		this.workFlow = workFlow;
		this.whdId = whdId;
		this.statusBorrow = statusBorrow;
		this.importer = importer;
		this.statusAppPayBack = statusAppPayBack;
		this.appPayBackDateFrom = appPayBackDateFrom;
		this.appPayBackDateTo = appPayBackDateTo;
		this.appDateFrom = appDateFrom;
		this.appDateTo = appDateTo;
	}

	public String getProductGroupId() {
		return productGroupId;
	}

	public void setProductGroupId(String productGroupId) {
		this.productGroupId = productGroupId;
	}

	public Integer getDocTypeId() {
		return docTypeId;
	}

	public void setDocTypeId(Integer docTypeId) {
		this.docTypeId = docTypeId;
	}

	public String getIdentityNum() {
		return identityNum;
	}

	public void setIdentityNum(String identityNum) {
		this.identityNum = identityNum;
	}

	public String getContractNum() {
		return contractNum;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}

	public String getAppNum() {
		return appNum;
	}

	public void setAppNum(String appNum) {
		this.appNum = appNum;
	}

	public String getFrameNum() {
		return frameNum;
	}

	public void setFrameNum(String frameNum) {
		this.frameNum = frameNum;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public String getReceiveDateFrom() {
		return receiveDateFrom;
	}

	public void setReceiveDateFrom(String receiveDateFrom) {
		this.receiveDateFrom = receiveDateFrom;
	}

	public String getReceiveDateTo() {
		return receiveDateTo;
	}

	public void setReceiveDateTo(String receiveDateTo) {
		this.receiveDateTo = receiveDateTo;
	}

	public String getAssignDateFrom() {
		return assignDateFrom;
	}

	public void setAssignDateFrom(String assignDateFrom) {
		this.assignDateFrom = assignDateFrom;
	}

	public String getAssignDateTo() {
		return assignDateTo;
	}

	public void setAssignDateTo(String assignDateTo) {
		this.assignDateTo = assignDateTo;
	}

	public Integer getAssigneeId() {
		return assigneeId;
	}

	public void setAssigneeId(Integer assigneeId) {
		this.assigneeId = assigneeId;
	}

	public Integer getAssignType() {
		return assignType;
	}

	public void setAssignType(Integer assignType) {
		this.assignType = assignType;
	}

	public Integer getStatusProcess() {
		return statusProcess;
	}

	public void setStatusProcess(Integer statusProcess) {
		this.statusProcess = statusProcess;
	}

	public String getStorageStatus() {
		return storageStatus;
	}

	public void setStorageStatus(String storageStatus) {
		this.storageStatus = storageStatus;
	}

	public String getProcessDateFrom() {
		return processDateFrom;
	}

	public void setProcessDateFrom(String processDateFrom) {
		this.processDateFrom = processDateFrom;
	}

	public String getProcessDateTo() {
		return processDateTo;
	}

	public void setProcessDateTo(String processDateTo) {
		this.processDateTo = processDateTo;
	}

	public String getLodgeDateFrom() {
		return lodgeDateFrom;
	}

	public void setLodgeDateFrom(String lodgeDateFrom) {
		this.lodgeDateFrom = lodgeDateFrom;
	}

	public String getLodgeDateTo() {
		return lodgeDateTo;
	}

	public void setLodgeDateTo(String lodgeDateTo) {
		this.lodgeDateTo = lodgeDateTo;
	}

	public String getBorrowDateFrom() {
		return borrowDateFrom;
	}

	public void setBorrowDateFrom(String borrowDateFrom) {
		this.borrowDateFrom = borrowDateFrom;
	}

	public String getBorrowDateTo() {
		return borrowDateTo;
	}

	public void setBorrowDateTo(String borrowDateTo) {
		this.borrowDateTo = borrowDateTo;
	}

	public String getBorrowApproveDateFrom() {
		return borrowApproveDateFrom;
	}

	public void setBorrowApproveDateFrom(String borrowApproveDateFrom) {
		this.borrowApproveDateFrom = borrowApproveDateFrom;
	}

	public String getBorrowApproveDateTo() {
		return borrowApproveDateTo;
	}

	public void setBorrowApproveDateTo(String borrowApproveDateTo) {
		this.borrowApproveDateTo = borrowApproveDateTo;
	}

	public String getToApproveDateFrom() {
		return toApproveDateFrom;
	}

	public void setToApproveDateFrom(String toApproveDateFrom) {
		this.toApproveDateFrom = toApproveDateFrom;
	}

	public String getToApproveDateTo() {
		return toApproveDateTo;
	}

	public void setToApproveDateTo(String toApproveDateTo) {
		this.toApproveDateTo = toApproveDateTo;
	}

	public Integer getStatusAllocate() {
		return statusAllocate;
	}

	public void setStatusAllocate(Integer statusAllocate) {
		this.statusAllocate = statusAllocate;
	}

	public String getWorkFlow() {
		return workFlow;
	}

	public void setWorkFlow(String workFlow) {
		this.workFlow = workFlow;
	}

	public Long getWhdId() {
		return whdId;
	}

	public void setWhdId(Long whdId) {
		this.whdId = whdId;
	}

	public String getStatusBorrow() {
		return statusBorrow;
	}

	public void setStatusBorrow(String statusBorrow) {
		this.statusBorrow = statusBorrow;
	}

	public String getNumPlate() {
		return numPlate;
	}

	public void setNumPlate(String numPlate) {
		this.numPlate = numPlate;
	}

	public String getImporter() {
		return importer;
	}

	public void setImporter(String importer) {
		this.importer = importer;
	}

	public String getStatusAppPayBack() {
		return statusAppPayBack;
	}

	public void setStatusAppPayBack(String statusAppPayBack) {
		this.statusAppPayBack = statusAppPayBack;
	}

	public String getAppPayBackDateFrom() {
		return appPayBackDateFrom;
	}

	public void setAppPayBackDateFrom(String appPayBackDateFrom) {
		this.appPayBackDateFrom = appPayBackDateFrom;
	}

	public String getAppPayBackDateTo() {
		return appPayBackDateTo;
	}

	public void setAppPayBackDateTo(String appPayBackDateTo) {
		this.appPayBackDateTo = appPayBackDateTo;
	}

	public String getAppDateFrom() {
		return appDateFrom;
	}

	public void setAppDateFrom(String appDateFrom) {
		this.appDateFrom = appDateFrom;
	}

	public String getAppDateTo() {
		return appDateTo;
	}

	public void setAppDateTo(String appDateTo) {
		this.appDateTo = appDateTo;
	}

}
