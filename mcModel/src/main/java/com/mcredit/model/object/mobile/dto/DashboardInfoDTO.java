package com.mcredit.model.object.mobile.dto;

public class DashboardInfoDTO {
	private Long caseProcessingNumber;
	private Long caseAbortNumber;
	
	public Long getCaseProcessingNumber() {
		return caseProcessingNumber;
	}
	public void setCaseProcessingNumber(Long caseProcessingNumber) {
		this.caseProcessingNumber = caseProcessingNumber;
	}
	public Long getCaseAbortNumber() {
		return caseAbortNumber;
	}
	public void setCaseAbortNumber(Long caseAbortNumber) {
		this.caseAbortNumber = caseAbortNumber;
	}
}
