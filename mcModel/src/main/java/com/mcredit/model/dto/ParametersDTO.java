package com.mcredit.model.dto;

import java.io.Serializable;

public class ParametersDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String paramName;
	private String paramValue;
	private String paramDataType;
	private String paramDescription;
	private String status;
	
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