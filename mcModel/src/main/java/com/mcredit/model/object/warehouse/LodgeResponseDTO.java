package com.mcredit.model.object.warehouse;

import java.io.Serializable;

public class LodgeResponseDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6070142413752140825L;
	
	private String lodgeCode;
	private String status;
	
	public String getLodgeCode() {
		return lodgeCode==null?"":lodgeCode;
	}
	public void setLodgeCode(String lodgeCode) {
		this.lodgeCode = lodgeCode;
	}
	public String getStatus() {
		return status==null?"":status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
