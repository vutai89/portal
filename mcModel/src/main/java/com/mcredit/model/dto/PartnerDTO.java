package com.mcredit.model.dto;

import java.io.Serializable;
import java.util.Date;

public class PartnerDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String recordStatus;
	private Date createdDate;
	private Date lastUpdatedDate;
	private String createdBy;
	private String lastUpdatedBy;
	private String partnerType;
	private String partnerCode;
	private String partnerName;
	private Integer partnerBank;
	private String partnerAcctInCore;
	private String accountingCode;
	private String status;
	private Integer bpmRefId;
	
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