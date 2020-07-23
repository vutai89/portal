package com.mcredit.model.dto;

import java.io.Serializable;

public class CodeTableEliteDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7262155118842302979L;
	
	private Integer id;
	private String codeValue1;
	private String codeValue2;
	private String description1;
	private String description2;
	private Integer parentId;
	private String reference;
	private String status;
	private String startEffDate;
	private String endEffDate;

	public CodeTableEliteDTO(){}
	
	public CodeTableEliteDTO(Integer id, String codeValue1, String description1, String codeValue2, String description2
			, Integer parentId, String reference, String status, String startEffDate, String endEffDate){
		this.id = id;
		this.codeValue1 = codeValue1;
		this.description1 = description1;
		this.codeValue2 = codeValue2;
		this.description2 = description2;
		this.parentId = parentId;
		this.reference = reference;
		this.status = status;
		this.startEffDate = startEffDate;
		this.endEffDate = endEffDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStartEffDate() {
		return startEffDate;
	}

	public void setStartEffDate(String startEffDate) {
		this.startEffDate = startEffDate;
	}

	public String getEndEffDate() {
		return endEffDate;
	}

	public void setEndEffDate(String endEffDate) {
		this.endEffDate = endEffDate;
	}

	public String getCodeValue2() {
		return codeValue2;
	}

	public void setCodeValue2(String codeValue2) {
		this.codeValue2 = codeValue2;
	}

	public String getDescription2() {
		return description2;
	}

	public void setDescription2(String description2) {
		this.description2 = description2;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCodeValue1() {
		return codeValue1;
	}

	public void setCodeValue1(String codeValue1) {
		this.codeValue1 = codeValue1;
	}

	public String getDescription1() {
		return description1;
	}

	public void setDescription1(String description1) {
		this.description1 = description1;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
}