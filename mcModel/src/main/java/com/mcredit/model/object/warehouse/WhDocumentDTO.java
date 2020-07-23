package com.mcredit.model.object.warehouse;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class WhDocumentDTO implements Serializable {
	
	private static final long serialVersionUID = 8024093738999078147L;

	private Long id;

	private Long version;

	private Long creditAppId;

	private Long docType;
	
	private String docTypename;

	private Long batchId;

	private Long orderBy;

	private Date estimateDate;

	private Long whCodeId;

	private Date whLodgeDate;

	private Date createdDate;

	private String createdBy;

	private String lastUpdatedBy;

	private Date lastUpdatedDate;

	private Long contractCavetType;

	private Long status;

	private String whLodgeBy;

	private Integer processStatus;

	private String billCode;

	private String deliveryError;

	private Date processDate;

	private Integer isActive;

	private Integer isOriginal;

	public List<WhDocumentErrDTO> lstWHCaseError;

	public List<WhDocumentErrDTO> lstWHCavetError;

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
	
	public String getDocTypename() {
		return docTypename;
	}

	public void setDocTypename(String docTypename) {
		this.docTypename = docTypename;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public Long getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}

	public Date getEstimateDate() {
		return estimateDate;
	}

	public void setEstimateDate(Date estimateDate) {
		this.estimateDate = estimateDate;
	}

	public Long getWhCodeId() {
		return whCodeId;
	}

	public void setWhCodeId(Long whCodeId) {
		this.whCodeId = whCodeId;
	}

	public Date getWhLodgeDate() {
		return whLodgeDate;
	}

	public void setWhLodgeDate(Date whLodgeDate) {
		this.whLodgeDate = whLodgeDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public Long getContractCavetType() {
		return contractCavetType;
	}

	public void setContractCavetType(Long contractCavetType) {
		this.contractCavetType = contractCavetType;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public String getWhLodgeBy() {
		return whLodgeBy;
	}

	public void setWhLodgeBy(String whLodgeBy) {
		this.whLodgeBy = whLodgeBy;
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

	public Date getProcessDate() {
		return processDate;
	}

	public void setProcessDate(Date processDate) {
		this.processDate = processDate;
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

	public List<WhDocumentErrDTO> getLstWHCaseError() {
		return lstWHCaseError;
	}

	public void setLstWHCaseError(List<WhDocumentErrDTO> lstWHCaseError) {
		this.lstWHCaseError = lstWHCaseError;
	}

	public List<WhDocumentErrDTO> getLstWHCavetError() {
		return lstWHCavetError;
	}

	public void setLstWHCavetError(List<WhDocumentErrDTO> lstWHCavetError) {
		this.lstWHCavetError = lstWHCavetError;
	}

}
