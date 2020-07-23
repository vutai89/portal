package com.mcredit.data.credit.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;


/**
 * The persistent class for the CREDIT_APP_LMS database table.
 * 
 */
@Entity
@Table(name="CREDIT_APP_LMS", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
@NamedNativeQuery(name="CreditAppLMS.nextSeq",query="select SEQ_CREDIT_APP_LMS_ID.NEXTVAL from DUAL")
public class CreditApplicationLoanManagement implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	@Column(name="CORE_LN_APP_ID")
	private String coreLnAppId;

	@Column(name="CREATED_BY")
	private String createdBy;

	@CreationTimestamp
	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DATE", updatable=false)
	private Date createdDate;

	@Column(name="CREDIT_APP_ID")
	private Long creditAppId;

	@Column(name="DISBURSEMENT_AMOUNT")
	private Long disbursementAmount;

	@Temporal(TemporalType.DATE)
	@Column(name="DISBURSEMENT_DATE")
	private Date disbursementDate;

	@Column(name="DISBURSEMENT_STATUS")
	private Integer disbursementStatus;

	@Temporal(TemporalType.DATE)
	@Column(name="FIRST_PAYMENT_DATE")
	private Date firstPaymentDate;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_PAYMENT_DATE")
	private Date lastPaymentDate;

	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@UpdateTimestamp
	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPDATED_DATE")
	private Date lastUpdatedDate;

	@Column(name="RECORD_STATUS")
	private String recordStatus;
	
	@Column(name="CONTRACT_STATUS_ID")
	private Integer contractStatusId;

	public CreditApplicationLoanManagement() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getContractStatusId() {
		return contractStatusId;
	}

	public void setContractStatusId(Integer contractStatusId) {
		this.contractStatusId = contractStatusId;
	}
	
	public String getCoreLnAppId() {
		return coreLnAppId;
	}

	public void setCoreLnAppId(String coreLnAppId) {
		this.coreLnAppId = coreLnAppId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getCreditAppId() {
		return creditAppId;
	}

	public void setCreditAppId(Long creditAppId) {
		this.creditAppId = creditAppId;
	}

	public Long getDisbursementAmount() {
		return disbursementAmount;
	}

	public void setDisbursementAmount(Long disbursementAmount) {
		this.disbursementAmount = disbursementAmount;
	}

	public Date getDisbursementDate() {
		return disbursementDate;
	}

	public void setDisbursementDate(Date disbursementDate) {
		this.disbursementDate = disbursementDate;
	}

	public Integer getDisbursementStatus() {
		return disbursementStatus;
	}

	public void setDisbursementStatus(Integer disbursementStatus) {
		this.disbursementStatus = disbursementStatus;
	}

	public Date getFirstPaymentDate() {
		return firstPaymentDate;
	}

	public void setFirstPaymentDate(Date firstPaymentDate) {
		this.firstPaymentDate = firstPaymentDate;
	}

	public Date getLastPaymentDate() {
		return lastPaymentDate;
	}

	public void setLastPaymentDate(Date lastPaymentDate) {
		this.lastPaymentDate = lastPaymentDate;
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

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


}