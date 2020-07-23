package com.mcredit.model.dto.check_cat;

import java.util.List;

public class ResponseCompTypeDTO {
	private List<CompanyDTO> lstComp;
	private List<String> errors;
	private String message;
	public List<CompanyDTO> getLstComp() {
		return lstComp;
	}
	public void setLstComp(List<CompanyDTO> lstComp) {
		this.lstComp = lstComp;
	}
	public List<String> getErrors() {
		return errors;
	}
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
