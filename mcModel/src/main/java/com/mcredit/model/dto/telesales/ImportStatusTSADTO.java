package com.mcredit.model.dto.telesales;

public class ImportStatusTSADTO {
	Long uplDetailId;
	String uplCode;
	public Long getUplDetailId() {
		return uplDetailId;
	}
	public void setUplDetailId(Long uplDetailId) {
		this.uplDetailId = uplDetailId;
	}
	public String getUplCode() {
		return uplCode;
	}
	public void setUplCode(String uplCode) {
		this.uplCode = uplCode;
	}
	
	public ImportStatusTSADTO(Long uplDetailId, String uplCode) {
		this.uplDetailId = uplDetailId;
		this.uplCode = uplCode;
	}
	
	
}
