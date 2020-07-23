package com.mcredit.business.job.cardPayment.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MakePaymentMCreditCardResultDTO {
	@SerializedName("error_status")
	@Expose
	private String errorStatus;
	
	@SerializedName("ftid")
	@Expose
	private String ftid;
	
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

	public String getFTID() {
		return ftid;
	}

	public void setFTID(String ftid) {
		this.ftid = ftid;
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
