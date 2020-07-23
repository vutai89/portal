package com.mcredit.model.object.warehouse;

import java.io.Serializable;
import java.util.List;

public class LodgeDocumentDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6070142413752140825L;
	
	private String type;
	private List<LodgeDocumentData> lstData;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<LodgeDocumentData> getLstData() {
		return lstData;
	}
	public void setLstData(List<LodgeDocumentData> lstData) {
		this.lstData = lstData;
	}

}
