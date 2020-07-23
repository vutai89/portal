package com.mcredit.model.dto.telesales;

public class ImportStatusDTO {
	Long UplDetailId;
	String status;
	String errorMessage;
	Integer dupOldData;
	Integer dupCurrentData;
	Integer invalidData;
	
	
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public Long getUplDetailId() {
		return UplDetailId;
	}
	public void setUplDetailId(Long uplDetailId) {
		UplDetailId = uplDetailId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getDupOldData() {
		return dupOldData;
	}
	public void setDupOldData(Integer dupOldData) {
		this.dupOldData = dupOldData;
	}
	public Integer getDupCurrentData() {
		return dupCurrentData;
	}
	public void setDupCurrentData(Integer dupCurrentData) {
		this.dupCurrentData = dupCurrentData;
	}
	public Integer getInvalidData() {
		return invalidData;
	}
	public void setInvalidData(Integer invalidData) {
		this.invalidData = invalidData;
	}
}
