package com.mcredit.model.object.warehouse;

public class ReturnDocumentInfo {
	public Long whId;
	public Long docTypeId;
	public String docTypeCode;
	public String docTypeName;
	public String createdDate;
	public Long statusId;
	public String statusCode;
	public String statusName;
	public String whCode;
	public Long contractCavetType;
	public Integer isActive;
	public Integer isOriginal;
	public Integer appendixContract;

	public Long getWhId() {
		return whId;
	}

	public void setWhId(Long whId) {
		this.whId = whId;
	}

	public Long getDocTypeId() {
		return docTypeId;
	}

	public void setDocTypeId(Long docTypeId) {
		this.docTypeId = docTypeId;
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

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getWhCode() {
		return whCode;
	}

	public void setWhCode(String whCode) {
		this.whCode = whCode;
	}

	public Long getContractCavetType() {
		return contractCavetType;
	}

	public void setContractCavetType(Long contractCavetType) {
		this.contractCavetType = contractCavetType;
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

}
