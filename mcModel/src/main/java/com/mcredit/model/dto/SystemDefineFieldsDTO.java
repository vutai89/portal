package com.mcredit.model.dto;

import java.io.Serializable;

public class SystemDefineFieldsDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String system;
	private Integer codeTableId;
	private String category;
	private String codeTableValue;
	private String systemValue;
	private String description;
	private String status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public Integer getCodeTableId() {
		return codeTableId;
	}

	public void setCodeTableId(Integer codeTableId) {
		this.codeTableId = codeTableId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCodeTableValue() {
		return codeTableValue;
	}

	public void setCodeTableValue(String codeTableValue) {
		this.codeTableValue = codeTableValue;
	}

	public String getSystemValue() {
		return systemValue;
	}

	public void setSystemValue(String systemValue) {
		this.systemValue = systemValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}