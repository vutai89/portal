package com.mcredit.data.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the SYSTEM_DEFINE_FIELDS database table.
 * 
 */
@Entity(name = "SystemDefineFields")
@Table(name = "SYSTEM_DEFINE_FIELDS")
@NamedQuery(name = "SystemDefineFields.findAll", query = "SELECT c FROM SystemDefineFields c")
public class SystemDefineFields implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private Integer id;

	@Column(name = "SYSTEM")
	private String system;

	@Column(name = "CODE_TABLE_ID")
	private Integer codeTableId;

	@Column(name = "CATEGORY")
	private String category;

	@Column(name = "CODE_TABLE_VALUE")
	private String codeTableValue;

	@Column(name = "SYSTEM_VALUE")
	private String systemValue;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "STATUS")
	private String status;

	public SystemDefineFields() {
	}

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