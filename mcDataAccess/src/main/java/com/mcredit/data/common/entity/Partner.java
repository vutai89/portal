package com.mcredit.data.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * The persistent class for the PARTNER database table.
 * 
 */
@Entity(name = "Partner")
@Table(name = "PARTNER", uniqueConstraints = { @UniqueConstraint(columnNames = { "ID" }) })
public class Partner implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "ID", unique = true)
	private Long id;

	@Column(name = "RECORD_STATUS")
	private String recordStatus;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "LAST_UPDATED_DATE")
	private Date lastUpdatedDate;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Column(name = "PARTNER_TYPE")
	private String partnerType;

	@Column(name = "PARTNER_CODE")
	private String partnerCode;

	@Column(name = "PARTNER_NAME")
	private String partnerName;

	@Column(name = "PARTNER_BANK")
	private Integer partnerBank;

	@Column(name = "PARTNER_ACCT_IN_CORE")
	private String partnerAcctInCore;

	@Column(name = "ACCOUNTING_CODE")
	private String accountingCode;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "BPM_REF_ID")
	private Integer bpmRefId;

	public Partner() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
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

	public String getPartnerType() {
		return partnerType;
	}

	public void setPartnerType(String partnerType) {
		this.partnerType = partnerType;
	}

	public String getPartnerCode() {
		return partnerCode;
	}

	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public Integer getPartnerBank() {
		return partnerBank;
	}

	public void setPartnerBank(Integer partnerBank) {
		this.partnerBank = partnerBank;
	}

	public String getPartnerAcctInCore() {
		return partnerAcctInCore;
	}

	public void setPartnerAcctInCore(String partnerAcctInCore) {
		this.partnerAcctInCore = partnerAcctInCore;
	}

	public String getAccountingCode() {
		return accountingCode;
	}

	public void setAccountingCode(String accountingCode) {
		this.accountingCode = accountingCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getBpmRefId() {
		return bpmRefId;
	}

	public void setBpmRefId(Integer bpmRefId) {
		this.bpmRefId = bpmRefId;
	}
}