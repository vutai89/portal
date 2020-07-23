package com.mcredit.model.dto.cic;

public class LeadDataTSReportDTO  {
	
	private String id;		// refId
	private String bankCode;
	private String bankApiKey;
	private String status;
	private String detail;
	
	public LeadDataTSReportDTO(String id, String bankCode, String bankApiKey, String status, String detail) {
		this.id = id;
		this.bankCode = bankCode;
		this.bankApiKey = bankApiKey;
		this.status = status;
		this.detail = detail;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getBankCode() {
		return bankCode;
	}
	
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	
	public String getBankApiKey() {
		return bankApiKey;
	}
	
	public void setBankApiKey(String bankApiKey) {
		this.bankApiKey = bankApiKey;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getDetail() {
		return detail;
	}
	
	public void setDetail(String detail) {
		this.detail = detail;
	}
	
}
