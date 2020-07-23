package com.mcredit.business.job.createCard.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class UpdateCaseOnBPMResultDTO {
	 
	
	
	@SerializedName("error_status")
	@Expose
	private String errorStatus;

	@SerializedName("customerid")
	@Expose
	private String customerid;

	@SerializedName("returnCode")
	@Expose
	private String returnCode;
	@SerializedName("returnMes")
	@Expose
	private String returnMes;

	public String getErrorStatus() {
		return errorStatus;
	}

	public void setErrorStatus(String errorStatus) {
		this.errorStatus = errorStatus;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
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
		
	public String getName(){		
		return "Result";
	}
	
	public String toJson(){
		return "{" + (errorStatus != null ? "\"coreCustCode\":\"" + errorStatus  : "")
				+ (customerid != null ? "\", \"customerid\":\"" + customerid : "")
				+ (returnCode != null ? "\",\"returnCode\":\"" + returnCode  : "")
				+ (returnMes != null ? "\",\"returnMes\":\"" + returnMes : "") + "\"}";
	}

}
