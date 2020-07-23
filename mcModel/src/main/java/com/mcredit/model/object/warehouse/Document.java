package com.mcredit.model.object.warehouse;

import java.util.Date;

public class Document {

	private Long id;

	private Long version;

	private Long creditAppId;

	private Long docType;

	private Long batchId;
	
	private Date estimateDate;

	private Long orderBy;

	private Long status;

	private Long whCodeId;

	private String whLodgeBy;
	
	private Date whLodgeDate;

	private Long contractCavetType;
	
	private Date processDate;
	
	private Integer processStatus;
	
	private String billCode;
	
	private String deliveryError;
	
	private Integer isActive;
	
	private Integer isOriginal;
	
	private Integer appendixContract;
	
	private String note;

	// wh_document_change
	private String type;

	private String createdBy;

	private String lastUpdatedBy;

	private String idCodeTable;
	
	private String statusCode;

	public Document() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Long getCreditAppId() {
		return creditAppId;
	}

	public void setCreditAppId(Long creditAppId) {
		this.creditAppId = creditAppId;
	}

	public Long getDocType() {
		return docType;
	}

	public void setDocType(Long docType) {
		this.docType = docType;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public Date getEstimateDate() {
		return estimateDate;
	}

	public void setEstimateDate(Date estimateDate) {
		this.estimateDate = estimateDate;
	}

	public Long getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Long getWhCodeId() {
		return whCodeId;
	}

	public void setWhCodeId(Long whCodeId) {
		this.whCodeId = whCodeId;
	}

	public String getWhLodgeBy() {
		return whLodgeBy;
	}

	public void setWhLodgeBy(String whLodgeBy) {
		this.whLodgeBy = whLodgeBy;
	}

	public Date getWhLodgeDate() {
		return whLodgeDate;
	}

	public void setWhLodgeDate(Date whLodgeDate) {
		this.whLodgeDate = whLodgeDate;
	}

	public Long getContractCavetType() {
		return contractCavetType;
	}

	public void setContractCavetType(Long contractCavetType) {
		this.contractCavetType = contractCavetType;
	}
	
	public Date getProcessDate() {
		return processDate;
	}

	public void setProcessDate(Date processDate) {
		this.processDate = processDate;
	}

	public Integer getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(Integer processStatus) {
		this.processStatus = processStatus;
	}

	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	public String getDeliveryError() {
		return deliveryError;
	}

	public void setDeliveryError(String deliveryError) {
		this.deliveryError = deliveryError;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
	
	public Integer getIsOriginal() {
		return isOriginal;
	}

	public void setIsOriginal(Integer isOriginal) {
		this.isOriginal = isOriginal;
	}

	public Integer getAppendixContract() {
		return appendixContract;
	}

	public void setAppendixContract(Integer appendixContract) {
		this.appendixContract = appendixContract;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getIdCodeTable() {
		return idCodeTable;
	}

	public void setIdCodeTable(String idCodeTable) {
		this.idCodeTable = idCodeTable;
	}
	
	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

}
