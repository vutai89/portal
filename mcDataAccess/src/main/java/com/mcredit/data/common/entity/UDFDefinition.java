package com.mcredit.data.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name="UDFDefinition")
@Table(name="UDF_DEFINITION")
public class UDFDefinition implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID")
	private Integer id;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DATE")
	private Date createdDate;

	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPDATED_DATE")
	private Date lastUpdatedDate;

	@Column(name="RECORD_STATUS")
	private String recordStatus;

	@Column(name="UDF_CODE")
	private String udfCode;

	@Column(name="UDF_LABEL")
	private String udfLabel;

	@Column(name="UDF_DATA_TYPE")
	private String udfDataType;

	@Column(name="UDF_TYPE")
	private String udfType;

	@Column(name="REGULAR_EXPRESSION")
	private String regularExpression;

	@Column(name="CATEGORY_CODE")
	private String categoryCode;

	@Column(name="MIN_LENGTH")
	private Integer minLength;

	@Column(name="MAX_LENGTH")
	private Integer maxLength;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getUdfCode() {
		return udfCode;
	}

	public void setUdfCode(String udfCode) {
		this.udfCode = udfCode;
	}

	public String getUdfLabel() {
		return udfLabel;
	}

	public void setUdfLabel(String udfLabel) {
		this.udfLabel = udfLabel;
	}

	public String getUdfDataType() {
		return udfDataType;
	}

	public void setUdfDataType(String udfDataType) {
		this.udfDataType = udfDataType;
	}

	public String getUdfType() {
		return udfType;
	}

	public void setUdfType(String udfType) {
		this.udfType = udfType;
	}

	public String getRegularExpression() {
		return regularExpression;
	}

	public void setRegularExpression(String regularExpression) {
		this.regularExpression = regularExpression;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public Integer getMinLength() {
		return minLength;
	}

	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

}
