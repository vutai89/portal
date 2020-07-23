package com.mcredit.model.object;

import java.io.Serializable;

public class ESBResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String returnCode;
	private String returnMes;

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMes() {
		return returnMes;
	}

	public void setReturnMes(String returnMes) {
		this.returnMes = returnMes;
	}
}
