package com.mcredit.data.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name="UDFProperties")
@Table(name="UDF_PROPERTIES")
public class UDFProperties implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID")
	private Integer id;

	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPDATED_DATE")
	private Date lastUpdatedDate;

	@Column(name="RECORD_STATUS")
	private String recordStatus;

	@Column(name="UDF_ID")
	private Integer udfId;

	@Column(name="FUNCTION_ID")
	private Integer functionId;

	@Column(name="UDF_PROPERTY_TYPE")
	private String udfPropertyType;

	@Column(name="UDF_PROPERTY_VALUE")
	private String udfPropertyValue;

	@Column(name="UDF_PROPERTY_NAME")
	private String udfPropertyName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getUdfId() {
		return udfId;
	}

	public void setUdfId(Integer udfId) {
		this.udfId = udfId;
	}

	public Integer getFunctionId() {
		return functionId;
	}

	public void setFunctionId(Integer functionId) {
		this.functionId = functionId;
	}

	public String getUdfPropertyType() {
		return udfPropertyType;
	}

	public void setUdfPropertyType(String udfPropertyType) {
		this.udfPropertyType = udfPropertyType;
	}

	public String getUdfPropertyValue() {
		return udfPropertyValue;
	}

	public void setUdfPropertyValue(String udfPropertyValue) {
		this.udfPropertyValue = udfPropertyValue;
	}

	public String getUdfPropertyName() {
		return udfPropertyName;
	}

	public void setUdfPropertyName(String udfPropertyName) {
		this.udfPropertyName = udfPropertyName;
	}

}
