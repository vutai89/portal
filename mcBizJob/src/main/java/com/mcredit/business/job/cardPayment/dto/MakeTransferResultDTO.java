package com.mcredit.business.job.cardPayment.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MakeTransferResultDTO {
	@SerializedName("error_status")
	@Expose
	private String errorStatus;
	
	@SerializedName("referenceNumber")
	@Expose
	private String referenceNumber;
	
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

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
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
