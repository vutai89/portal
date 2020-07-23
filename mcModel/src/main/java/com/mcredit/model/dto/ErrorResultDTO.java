package com.mcredit.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ErrorResultDTO {

	@SerializedName("returnCode")
	@Expose
	private String returnCode;

	@SerializedName("returnMes")
	@Expose
	private String returnMes;

	
	public ErrorResultDTO(String returnCode, String returnMes) {
		this.returnCode = returnCode;
		this.returnMes = returnMes;
	}
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