package com.mcredit.data.credit.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the CREDIT_APP_APPRAISAL database table.
 * 
 */
@Entity
@Table(name="CREDIT_APP_APPRAISAL", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
@NamedNativeQuery(name="CreditAppAppraisal.nextSeq",query="select SEQ_CREDIT_APP_APPRAISAL_ID.NEXTVAL from DUAL")
public class CreditApplicationAppraisal implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	@Column(name="APPROVED_AMOUNT")
	private BigDecimal approvedAmount;

	@Temporal(TemporalType.DATE)
	@Column(name="APPROVED_DATE")
	private Date approvedDate;

	@Column(name="APPROVED_USER")
	private Integer approvedUser;

	@Column(name="CREATED_BY")
	private String createdBy;
	
	@CreationTimestamp
	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DATE", updatable=false)
	private Date createdDate;

	@UpdateTimestamp
	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPDATED_DATE")
	private Date lastUpdatedDate;

	@Column(name="CREDIT_APP_ID")
	private Long creditAppId;

	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;


	@Column(name="RECORD_STATUS")
	private String recordStatus;
	
	@Column(name="INDUSTRY")
	private Integer industry;
	
	public CreditApplicationAppraisal() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getApprovedAmount() {
		return this.approvedAmount;
	}

	public void setApprovedAmount(BigDecimal approvedAmount) {
		this.approvedAmount = approvedAmount;
	}

	public Date getApprovedDate() {
		return this.approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public Integer getApprovedUser() {
		return this.approvedUser;
	}

	public void setApprovedUser(Integer approvedUser) {
		this.approvedUser = approvedUser;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getCreditAppId() {
		return this.creditAppId;
	}

	public void setCreditAppId(Long creditAppId) {
		this.creditAppId = creditAppId;
	}

	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdatedDate() {
		return this.lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getRecordStatus() {
		return this.recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Integer getIndustry() {
		return industry;
	}

	public void setIndustry(Integer industry) {
		this.industry = industry;
	}

}