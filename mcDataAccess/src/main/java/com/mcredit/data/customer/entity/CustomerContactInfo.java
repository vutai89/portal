package com.mcredit.data.customer.entity;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;


/**
 * The persistent class for the CUST_CONTACT_INFO database table.
 * 
 */
@Entity
@Table(name="CUST_CONTACT_INFO")
@NamedQuery(name="CustomerContactInfo.nextContactPriority",query = " select max(contactPriority) from CustomerContactInfo " +
				 												   " where custId = :custId and contactType = :contactType and contactCategory = :contactCategory ")
public class CustomerContactInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "seq_cci"
				    , strategy = "com.mcredit.data.common.entity.SequenceGenerate"
					, parameters = @Parameter(name = "seqName", value = "SEQ_CUST_CONTACT_INFO_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_cci")
	private Long id;
	
	@Column(name="CUST_ID")
	private Long custId;
	
	@Column(name="RECORD_STATUS")
	private String recordStatus;
	
	@Column(name="CREATED_BY", updatable=false)
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DATE", updatable=false)
	@CreationTimestamp
	private Date createdDate;
	
	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPDATED_DATE")
	@UpdateTimestamp
	private Date lastUpdatedDate;
	
	@Column(name="CONTACT_TYPE")
	private Integer contactType;

	@Column(name="CONTACT_CATEGORY")
	private Integer contactCategory;

	@Column(name="CONTACT_PRIORITY")
	private Integer contactPriority;

	@Column(name="CONTACT_VALUE")
	private String contactValue;

	public CustomerContactInfo() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
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
		return custId;
	}

	public void setCustId(Long custId) {
		this.custId = custId;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
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

	public Integer getContactType() {
		return contactType;
	}

	public void setContactType(Integer contactType) {
		this.contactType = contactType;
	}

	public Integer getContactCategory() {
		return contactCategory;
	}

	public void setContactCategory(Integer contactCategory) {
		this.contactCategory = contactCategory;
	}

	public Integer getContactPriority() {
		return contactPriority;
	}

	public void setContactPriority(Integer contactPriority) {
		this.contactPriority = contactPriority;
	}

	public String getContactValue() {
		return contactValue;
	}

	public void setContactValue(String contactValue) {
		this.contactValue = contactValue;
	}
}