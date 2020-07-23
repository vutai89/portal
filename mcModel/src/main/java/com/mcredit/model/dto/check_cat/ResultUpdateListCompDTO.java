package com.mcredit.model.dto.check_cat;

import java.util.List;

public class ResultUpdateListCompDTO {
	private int removeComp;
	private int insertComp;
	private List<String> lstErrors;
	public int getRemoveComp() {
		return removeComp;
	}
	public int getInsertComp() {
		return insertComp;
	}
	public void setRemoveComp(int removeComp) {
		this.removeComp = removeComp;
	}
	public void setInsertComp(int insertComp) {
		this.insertComp = insertComp;
	}
	public List<String> getLstErrors() {
		return lstErrors;
	}
	public void setLstErrors(List<String> lstErrors) {
		this.lstErrors = lstErrors;
	}
	
}
