package com.mcredit.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PostingConfigurationDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String recordStatus;
	private Date createdDate;
	private Date lastUpdatedDate;
	private String createdBy;
	private String lastUpdatedBy;
	private String postingGroup;
	private Integer postingOrder;
	private Integer transactionTypeId;
	private String debitOwner;
	private String debitBranch;
	private String debitCcy;
	private String debitAccount;
	private String creditOwner;
	private String creditBranch;
	private String creditCcy;
	private String creditAccount;
	private BigDecimal amount;
	private String amountTag;
	private String postingSystem;
	private Integer partnerId;

	public Integer getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
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
	public Integer getTransactionTypeId() {
		return transactionTypeId;
	}
	public void setTransactionTypeId(Integer transactionTypeId) {
		this.transactionTypeId = transactionTypeId;
	}
	public String getDebitOwner() {
		return debitOwner;
	}
	public void setDebitOwner(String debitOwner) {
		this.debitOwner = debitOwner;
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
	public String getDebitAccount() {
		return debitAccount;
	}
	public void setDebitAccount(String debitAccount) {
		this.debitAccount = debitAccount;
	}
	public String getCreditOwner() {
		return creditOwner;
	}
	public void setCreditOwner(String creditOwner) {
		this.creditOwner = creditOwner;
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
	public String getCreditAccount() {
		return creditAccount;
	}
	public void setCreditAccount(String creditAccount) {
		this.creditAccount = creditAccount;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getAmountTag() {
		return amountTag;
	}
	public void setAmountTag(String amountTag) {
		this.amountTag = amountTag;
	}
	public String getPostingSystem() {
		return postingSystem;
	}
	public void setPostingSystem(String postingSystem) {
		this.postingSystem = postingSystem;
	}
}