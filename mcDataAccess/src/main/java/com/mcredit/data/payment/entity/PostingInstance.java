package com.mcredit.data.payment.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;


/**
 * The persistent class for the POSTING_INSTANCE database table.
 * 
 */
@Entity
@Table(name="POSTING_INSTANCE")
@NamedQuery(name="PostingInstance.findAll", query="SELECT p FROM PostingInstance p")
public class PostingInstance implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "seq_pInstance" , strategy = "com.mcredit.data.common.entity.SequenceGenerate" , parameters = @Parameter(name = "seqName", value = "SEQ_POSTING_INSTANCE_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_pInstance") 
	private Long id;

	@Column(name="AMOUNT")
	private BigDecimal amount;
	
	@Column(name="POSTING_CONFIGURATION_ID")
	private Long postingConfigurationId;
	
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

	@Column(name="CUST_ID")
	private Long custId;

	@Column(name="DEBIT_ACCOUNT")
	private String debitAccount;

	@Column(name="DEBIT_BRANCH")
	private String debitBranch;

	@Column(name="DEBIT_CCY")
	private String debitCcy;

	@Column(name="DEBIT_OWNER")
	private String debitOwner;

	@Column(name="FT_REF_NUMBER")
	private String ftRefNumber;

	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Column(name="LAST_UPDATED_DATE")
	private Timestamp lastUpdatedDate;

	@Column(name="POSTING_GROUP")
	private String postingGroup;

	@Column(name="POSTING_ORDER")
	private Integer postingOrder;

	@Column(name="PARTNER_ID")
	private Integer partnerId;
	
	@Column(name="RECORD_STATUS")
	private String recordStatus;

	private String status;

	@Column(name="TRANSACTION_TYPE_ID")
	private Long transactionType;
	
	@Column(name = "PARTNER_REF_ID")
	private String partnerRefId;

	public PostingInstance() {
	}

	public Long getCustId() {
		return custId;
	}

	public void setCustId(Long custId) {
		this.custId = custId;
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}
	
	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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

	public String getFtRefNumber() {
		return this.ftRefNumber;
	}

	public void setFtRefNumber(String ftRefNumber) {
		this.ftRefNumber = ftRefNumber;
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

	public Integer getPostingOrder() {
		return this.postingOrder;
	}

	public void setPostingOrder(Integer postingOrder) {
		this.postingOrder = postingOrder;
	}

	public String getRecordStatus() {
		return this.recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getTransactionType() {
		return this.transactionType;
	}

	public void setTransactionType(Long transactionType) {
		this.transactionType = transactionType;
	}
	
	public Long getPostingConfigurationId() {
		return postingConfigurationId;
	}

	public void setPostingConfigurationId(Long postingConfigurationId) {
		this.postingConfigurationId = postingConfigurationId;
	}

	public String getPartnerRefId() {
		return partnerRefId;
	}

	public void setPartnerRefId(String partnerRefId) {
		this.partnerRefId = partnerRefId;
	}

}