package com.mcredit.product.dto;

public class ComboboxDTO {

	public ComboboxDTO(String code, String name, String status) {
		// TODO Auto-generated constructor stub
		this.code = code;
		this.name = name;
		this.status = status;
	}

	// Chi bao gom id, name, code
	private String code;
	private String name;
	private String status;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
