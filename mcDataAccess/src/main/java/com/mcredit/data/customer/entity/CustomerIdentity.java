package com.mcredit.data.customer.entity;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;


/**
 * The persistent class for the CUST_IDENTITY database table.
 * 
 */
@Entity
@Table(name="CUST_IDENTITY")
public class CustomerIdentity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "seq_ci"
				    , strategy = "com.mcredit.data.common.entity.SequenceGenerate"
					, parameters = @Parameter(name = "seqName", value = "SEQ_CUST_IDENTITY_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_ci")
	private Long id;

	@Column(name="CREATED_BY", updatable=false)
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DATE", updatable=false)
	@CreationTimestamp
	private Date createdDate;

	@Column(name="CUST_ID")
	private Long custId;

	@Temporal(TemporalType.DATE)
	@Column(name="IDENTITY_EXPIRY_DATE")
	private Date identityExpiryDate;

	@Temporal(TemporalType.DATE)
	@Column(name="IDENTITY_ISSUE_DATE")
	private Date identityIssueDate;

	@Column(name="IDENTITY_ISSUE_PLACE")
	private Integer identityIssuePlace;

	@Column(name="IDENTITY_NUMBER")
	private String identityNumber;

	@Column(name="IDENTITY_TYPE_ID")
	private Integer identityTypeId;

	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPDATED_DATE")
	@UpdateTimestamp
	private Date lastUpdatedDate;

	@Column(name="RECORD_STATUS")
	private String recordStatus;
	
	@Transient
	private String identityTypeIdValue;
	
	public CustomerIdentity() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getIdentityTypeIdValue() {
		return identityTypeIdValue;
	}

	public void setIdentityTypeIdValue(String identityTypeIdValue) {
		this.identityTypeIdValue = identityTypeIdValue;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy!=null?createdBy.trim():null;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getCustId() {
		return this.custId;
	}

	public void setCustId(Long custId) {
		this.custId = custId;
	}

	public Date getIdentityExpiryDate() {
		return this.identityExpiryDate;
	}

	public void setIdentityExpiryDate(Date identityExpiryDate) {
		this.identityExpiryDate = identityExpiryDate;
	}

	public Date getIdentityIssueDate() {
		return this.identityIssueDate;
	}

	public void setIdentityIssueDate(Date identityIssueDate) {
		this.identityIssueDate = identityIssueDate;
	}

	public Integer getIdentityIssuePlace() {
		return this.identityIssuePlace;
	}

	public void setIdentityIssuePlace(Integer identityIssuePlace) {
		this.identityIssuePlace = identityIssuePlace;
	}

	public String getIdentityNumber() {
		return this.identityNumber;
	}

	public void setIdentityNumber(String identityNumber) {
		this.identityNumber = identityNumber!=null?identityNumber.trim():null;
	}

	public Integer getIdentityTypeId() {
		return this.identityTypeId;
	}

	public void setIdentityTypeId(Integer identityTypeId) {
		this.identityTypeId = identityTypeId;
	}

	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy!=null?lastUpdatedBy.trim():null;
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
		this.recordStatus = recordStatus!=null?recordStatus.trim():null;
	}
}