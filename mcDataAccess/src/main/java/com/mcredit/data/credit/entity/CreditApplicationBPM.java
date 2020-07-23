package com.mcredit.data.credit.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;


/**
 * The persistent class for the CREDIT_APP_BPM database table.
 * 
 */
@Entity
@Table(name="CREDIT_APP_BPM", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
@NamedNativeQuery(name="CreditAppBPM.nextSeq",query="select SEQ_CREDIT_APP_BPM_ID.NEXTVAL from DUAL")
public class CreditApplicationBPM implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	@Column(name="BPM_APP_ID")
	private String bpmAppId;

	@Column(name="BPM_APP_NUMBER")
	private Integer bpmAppNumber;

	@Column(name="BPM_ID")
	private Long bpmId;

	@Column(name="CREATED_BY")
	private String createdBy;

	@CreationTimestamp
	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DATE", updatable=false)
	private Date createdDate;

	@Column(name="CREDIT_APP_ID")
	private Long creditAppId;

	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@UpdateTimestamp
	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPDATED_DATE")
	private Date lastUpdatedDate;

	@Column(name="RECORD_STATUS")
	private String recordStatus;

	public CreditApplicationBPM() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBpmAppId() {
		return this.bpmAppId;
	}

	public void setBpmAppId(String bpmAppId) {
		this.bpmAppId = bpmAppId;
	}

	public Integer getBpmAppNumber() {
		return this.bpmAppNumber;
	}

	public void setBpmAppNumber(Integer bpmAppNumber) {
		this.bpmAppNumber = bpmAppNumber;
	}

	public Long getBpmId() {
		return this.bpmId;
	}

	public void setBpmId(Long bpmId) {
		this.bpmId = bpmId;
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

}