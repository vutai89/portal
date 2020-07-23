package com.mcredit.model.dto.check_cat;

import java.util.List;

public class UpdateTopCompDTO {
	private List<String> lstRemove;
	private List<CompInsertDTO> lstInsert;
	private String ctCat;
	
	public List<String> getLstRemove() {
		return lstRemove;
	}
	public List<CompInsertDTO> getLstInsert() {
		return lstInsert;
	}
	public String getCtCat() {
		return ctCat;
	}
	public void setLstRemove(List<String> lstRemove) {
		this.lstRemove = lstRemove;
	}
	public void setLstInsert(List<CompInsertDTO> lstInsert) {
		this.lstInsert = lstInsert;
	}
	public void setCtCat(String ctCat) {
		this.ctCat = ctCat;
	}
	
	

}
