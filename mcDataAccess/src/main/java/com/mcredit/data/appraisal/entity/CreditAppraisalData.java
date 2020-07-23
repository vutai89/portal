package com.mcredit.data.appraisal.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name="CREDIT_APPRAISAL_DATA")
public class CreditAppraisalData implements Serializable {
	@Id
	@GenericGenerator(name = "seq_CreditAppraisalData", strategy = "com.mcredit.data.common.entity.SequenceGenerate", parameters = @Parameter(name = "seqName", value = "SEQ_CREDIT_APPRAISAL_DATA_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_CreditAppraisalData")
	private Long id;
	private static final long serialVersionUID = 1L;
	
	@Column(name = "ACTION")
	private String action;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_UPDATED_DATE")
	private Date lastUpdatedDate;
	
	@Column(name = "CREATE_BY")
	private String createBy;

	@Column(name = "LAST_UPDATE_BY")
	private String lastUpdateBy;
	
	@Column(name = "BPM_APP_ID")
	private String bpmAppId;
	
	@Column(name = "APPRAISAL_DATA_DETAIL")
	private String appraisalDataDetail;
	
	@Column(name = "CONCLUDE")
	private String conclude;
	
	@Column(name = "TRANSACTION_ID")
	private String transactionId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String actor) {
		this.action = actor;
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

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getLastUpdateBy() {
		return lastUpdateBy;
	}

	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}

	public String getBpmAppId() {
		return bpmAppId;
	}

	public void setBpmAppId(String bpmAppId) {
		this.bpmAppId = bpmAppId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getAppraisalDataDetail() {
		return appraisalDataDetail;
	}

	public void setAppraisalDataDetail(String appraisalDataDetail) {
		this.appraisalDataDetail = appraisalDataDetail;
	}

	public String getConclude() {
		return conclude;
	}

	public void setConclude(String conclude) {
		this.conclude = conclude;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

}
