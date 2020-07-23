package com.mcredit.model.dto;

public class CodeTableComboboxDTO {

	public CodeTableComboboxDTO() {
		// TODO Auto-generated constructor stub
	}

	// Chi bao gom id, name, code
	private Long id;
	private String codeValue1;
	private String description1;
	private String status;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
