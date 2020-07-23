package com.mcredit.model.object.mobile.dto;

import java.util.List;

public class CreCaseBPMDTO {
	private List<InfoCreCaseDTO> variables;
	private String pro_uid;
	private String tas_uid;
	public List<InfoCreCaseDTO> getVariables() {
		return variables;
	}
	public void setVariables(List<InfoCreCaseDTO> variables) {
		this.variables = variables;
	}
	public String getPro_uid() {
		return pro_uid;
	}
	public void setPro_uid(String pro_uid) {
		this.pro_uid = pro_uid;
	}
	public String getTas_uid() {
		return tas_uid;
	}
	public void setTas_uid(String tas_uid) {
		this.tas_uid = tas_uid;
	}
	
}
