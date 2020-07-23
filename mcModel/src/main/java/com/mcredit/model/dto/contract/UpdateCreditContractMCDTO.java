package com.mcredit.model.dto.contract;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateCreditContractMCDTO {

	public UpdateCreditContractMCDTO(String contractNumber, String date) {
		this.contractNumber = contractNumber;
		this.date = date;
	}
	
	@SerializedName("ContractNumber")
	@Expose
	private String contractNumber;
	@SerializedName("Date")
	@Expose
	private String date;
	
	public String getContractNumber() {
		return contractNumber;
	}
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}