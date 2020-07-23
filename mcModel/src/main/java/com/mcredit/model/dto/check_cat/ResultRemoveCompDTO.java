package com.mcredit.model.dto.check_cat;

import java.util.List;

public class ResultRemoveCompDTO {
	private int numbCompRemove;
	private List<String> lstError;  
	
	
	public ResultRemoveCompDTO(int numbCompRemove, List<String> lstError) {
		super();
		this.numbCompRemove = numbCompRemove;
		this.lstError = lstError;
	}
	
	public int getNumbCompRemove() {
		return numbCompRemove;
	}
	public void setNumbCompRemove(int numbCompRemove) {
		this.numbCompRemove = numbCompRemove;
	}
	public List<String> getLstError() {
		return lstError;
	}
	public void setLstError(List<String> lstError) {
		this.lstError = lstError;
	}
}
