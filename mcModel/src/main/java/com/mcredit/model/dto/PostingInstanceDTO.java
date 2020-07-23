package com.mcredit.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class PostingInstanceDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;

	private BigDecimal amount;

	private Long postingConfigurationId;

	private String createdBy;

	private Timestamp createdDate;

	private String creditAccount;

	private String creditBranch;

	private String creditCcy;

	private String creditOwner;

	private Long custId;

	private String debitAccount;

	private String debitBranch;

	private String debitCcy;

	private String debitOwner;

	private String ftRefNumber;

	private String lastUpdatedBy;

	private Timestamp lastUpdatedDate;

	private String postingGroup;

	private Integer postingOrder;

	private Integer partnerId;

	private String recordStatus;

	private String status;

	private Long transactionType;

	private String partnerRefId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Long getPostingConfigurationId() {
		return postingConfigurationId;
	}

	public void setPostingConfigurationId(Long postingConfigurationId) {
		this.postingConfigurationId = postingConfigurationId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreditAccount() {
		return creditAccount;
	}

	public void setCreditAccount(String creditAccount) {
		this.creditAccount = creditAccount;
	}

	public String getCreditBranch() {
		return creditBranch;
	}

	public void setCreditBranch(String creditBranch) {
		this.creditBranch = creditBranch;
	}

	public String getCreditCcy() {
		return creditCcy;
	}

	public void setCreditCcy(String creditCcy) {
		this.creditCcy = creditCcy;
	}

	public String getCreditOwner() {
		return creditOwner;
	}

	public void setCreditOwner(String creditOwner) {
		this.creditOwner = creditOwner;
	}

	public Long getCustId() {
		return custId;
	}

	public void setCustId(Long custId) {
		this.custId = custId;
	}

	public String getDebitAccount() {
		return debitAccount;
	}

	public void setDebitAccount(String debitAccount) {
		this.debitAccount = debitAccount;
	}

	public String getDebitBranch() {
		return debitBranch;
	}

	public void setDebitBranch(String debitBranch) {
		this.debitBranch = debitBranch;
	}

	public String getDebitCcy() {
		return debitCcy;
	}

	public void setDebitCcy(String debitCcy) {
		this.debitCcy = debitCcy;
	}

	public String getDebitOwner() {
		return debitOwner;
	}

	public void setDebitOwner(String debitOwner) {
		this.debitOwner = debitOwner;
	}

	public String getFtRefNumber() {
		return ftRefNumber;
	}

	public void setFtRefNumber(String ftRefNumber) {
		this.ftRefNumber = ftRefNumber;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Timestamp getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Timestamp lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getPostingGroup() {
		return postingGroup;
	}

	public void setPostingGroup(String postingGroup) {
		this.postingGroup = postingGroup;
	}

	public Integer getPostingOrder() {
		return postingOrder;
	}

	public void setPostingOrder(Integer postingOrder) {
		this.postingOrder = postingOrder;
	}

	public Integer getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(Long transactionType) {
		this.transactionType = transactionType;
	}

	public String getPartnerRefId() {
		return partnerRefId;
	}

	public void setPartnerRefId(String partnerRefId) {
		this.partnerRefId = partnerRefId;
	}

}
