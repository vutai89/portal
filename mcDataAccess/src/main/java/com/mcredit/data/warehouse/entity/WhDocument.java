package com.mcredit.data.warehouse.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "WH_DOCUMENT")
// @NamedQuery(name = "WhDocument.findAll", query = "SELECT p FROM WhDocument
// p")
public class WhDocument implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6621985890166777721L;

	@Id
	@GenericGenerator(name = "seq_WhDocument", strategy = "com.mcredit.data.common.entity.SequenceGenerate", parameters = @Parameter(name = "seqName", value = "SEQ_WH_DOCUMENT_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_WhDocument")
	private Long id;

	@Column(name = "VERSION")
	private Long version;

	@Column(name = "CREDIT_APP_ID")
	private Long creditAppId;

	@Column(name = "DOC_TYPE")
	private Long docType;

	@Column(name = "BATCH_ID")
	private Long batchId;

	@Column(name = "ORDER_BY")
	private Long orderBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "ESTIMATE_DATE")
	private Date estimateDate;

	@Column(name = "WH_CODE_ID")
	private Long whCodeId;

	@Temporal(TemporalType.DATE)
	@Column(name = "WH_LODGE_DATE")
	private Date whLodgeDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_UPDATED_DATE")
	private Date lastUpdatedDate;

	@Column(name = "CONTRACT_CAVET_TYPE")
	private Long contractCavetType;

	@Column(name = "STATUS")
	private Long status;

	@Column(name = "WH_LODGE_BY")
	private String whLodgeBy;

	@Column(name = "PROCESS_STATUS")
	private Integer processStatus;

	@Column(name = "BILL_CODE")
	private String billCode;

	@Column(name = "DELIVERY_ERROR")
	private String deliveryError;

	@Temporal(TemporalType.DATE)
	@Column(name = "PROCESS_DATE")
	private Date processDate;

	@Column(name = "IS_ACTIVE")
	private Integer isActive;

	@Column(name = "IS_ORIGINAL")
	private Integer isOriginal;

	@Column(name = "APPENDIX_CONTRACT")
	private Integer appendixContract;

	@Column(name = "NOTE")
	private String note;

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

	public Date getProcessDate() {
		return processDate;
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

	public void setProcessDate(Date processDate) {
		this.processDate = processDate;
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public WhDocument(Long id, Long version, Long creditAppId, Long docType, Long batchId, Long orderBy,
			Date estimateDate, Long whCodeId, Date whLodgeDate, Date createdDate, String createdBy,
			String lastUpdatedBy, Date lastUpdatedDate, Long contractCavetType, Long status, String whLodgeBy,
			Integer processStatus, String billCode, String deliveryError, Date processDate, Integer isActive,
			Integer isOriginal, Integer appendixContract, String note) {

		super();
		this.id = id;
		this.version = version;
		this.creditAppId = creditAppId;
		this.docType = docType;
		this.batchId = batchId;
		this.orderBy = orderBy;
		this.estimateDate = estimateDate;
		this.whCodeId = whCodeId;
		this.whLodgeDate = whLodgeDate;
		this.createdDate = createdDate;
		this.createdBy = createdBy;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedDate = lastUpdatedDate;
		this.contractCavetType = contractCavetType;
		this.status = status;
		this.whLodgeBy = whLodgeBy;
		this.processStatus = processStatus;
		this.billCode = billCode;
		this.deliveryError = deliveryError;
		this.processDate = processDate;
		this.isActive = isActive;
		this.isOriginal = isOriginal;
		this.appendixContract = appendixContract;
		this.note = note;

	}

	public WhDocument() {
		super();
	}

}