package com.mcredit.data.payment.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;


/**
 * The persistent class for the POSTING_CONFIGURATION database table.
 * 
 */
@Entity
@Table(name="POSTING_CONFIGURATION")
@NamedQuery(name="PostingConfiguration.findAll", query="SELECT p FROM PostingConfiguration p")
public class PostingConfiguration implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long id;

	private BigDecimal amount;

	@Column(name="AMOUNT_TAG")
	private String amountTag;

	@Column(name="CREATED_BY")
	private String createdBy;
	
	@CreationTimestamp
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="CREDIT_ACCOUNT")
	private String creditAccount;

	@Column(name="CREDIT_BRANCH")
	private String creditBranch;

	@Column(name="CREDIT_CCY")
	private String creditCcy;

	@Column(name="CREDIT_OWNER")
	private String creditOwner;

	@Column(name="DEBIT_ACCOUNT")
	private String debitAccount;

	@Column(name="DEBIT_BRANCH")
	private String debitBranch;

	@Column(name="DEBIT_CCY")
	private String debitCcy;

	@Column(name="DEBIT_OWNER")
	private String debitOwner;

	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Column(name="LAST_UPDATED_DATE")
	private Timestamp lastUpdatedDate;

	@Column(name="POSTING_GROUP")
	private String postingGroup;

	@Column(name="POSTING_ORDER")
	private int postingOrder;

	@Column(name="POSTING_SYSTEM")
	private String postingSystem;

	@Column(name="RECORD_STATUS")
	private String recordStatus;

	@Column(name="TRANSACTION_TYPE_ID")
	private Integer transactionTypeId;
	
	@Column(name="PARTNER_ID")
	private Integer partnerId;

	public Integer getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}

	public PostingConfiguration() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getAmountTag() {
		return this.amountTag;
	}

	public void setAmountTag(String amountTag) {
		this.amountTag = amountTag;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreditAccount() {
		return this.creditAccount;
	}

	public void setCreditAccount(String creditAccount) {
		this.creditAccount = creditAccount;
	}

	public String getCreditBranch() {
		return this.creditBranch;
	}

	public void setCreditBranch(String creditBranch) {
		this.creditBranch = creditBranch;
	}

	public String getCreditCcy() {
		return this.creditCcy;
	}

	public void setCreditCcy(String creditCcy) {
		this.creditCcy = creditCcy;
	}

	public String getCreditOwner() {
		return this.creditOwner;
	}

	public void setCreditOwner(String creditOwner) {
		this.creditOwner = creditOwner;
	}

	public String getDebitAccount() {
		return this.debitAccount;
	}

	public void setDebitAccount(String debitAccount) {
		this.debitAccount = debitAccount;
	}

	public String getDebitBranch() {
		return this.debitBranch;
	}

	public void setDebitBranch(String debitBranch) {
		this.debitBranch = debitBranch;
	}

	public String getDebitCcy() {
		return this.debitCcy;
	}

	public void setDebitCcy(String debitCcy) {
		this.debitCcy = debitCcy;
	}

	public String getDebitOwner() {
		return this.debitOwner;
	}

	public void setDebitOwner(String debitOwner) {
		this.debitOwner = debitOwner;
	}

	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Timestamp getLastUpdatedDate() {
		return this.lastUpdatedDate;
	}

	public void setLastUpdatedDate(Timestamp lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getPostingGroup() {
		return this.postingGroup;
	}

	public void setPostingGroup(String postingGroup) {
		this.postingGroup = postingGroup;
	}

	public int getPostingOrder() {
		return this.postingOrder;
	}

	public void setPostingOrder(int postingOrder) {
		this.postingOrder = postingOrder;
	}

	public String getPostingSystem() {
		return this.postingSystem;
	}

	public void setPostingSystem(String postingSystem) {
		this.postingSystem = postingSystem;
	}

	public String getRecordStatus() {
		return this.recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Integer getTransactionTypeId() {
		return this.transactionTypeId;
	}

	public void setTransactionTypeId(Integer transactionTypeId) {
		this.transactionTypeId = transactionTypeId;
	}

}