package com.mcredit.data.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * The persistent class for the PARTNER database table.
 * 
 */
@Entity(name = "Parameters")
@Table(name = "PARAMETERS", uniqueConstraints = { @UniqueConstraint(columnNames = { "PARAM_NAME" }) })
public class Parameters implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PARAM_NAME", unique = true)
	private String paramName;

	@Column(name = "PARAM_VALUE")
	private String paramValue;

	@Column(name = "PARAM_DATA_TYPE")
	private String paramDataType;

	@Column(name = "PARAM_DESCRIPTION")
	private String paramDescription;

	@Column(name = "STATUS")
	private String status;

	public Parameters() {
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getParamDataType() {
		return paramDataType;
	}

	public void setParamDataType(String paramDataType) {
		this.paramDataType = paramDataType;
	}

	public String getParamDescription() {
		return paramDescription;
	}

	public void setParamDescription(String paramDescription) {
		this.paramDescription = paramDescription;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}