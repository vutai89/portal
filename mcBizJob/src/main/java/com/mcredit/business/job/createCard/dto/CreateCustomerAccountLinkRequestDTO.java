package com.mcredit.business.job.createCard.dto;

public class CreateCustomerAccountLinkRequestDTO {

	private String RequestId;
	private String BatchId;
	private String IssueId;
	private String DateInput;

	public String getRequestId() {
		return RequestId;
	}

	public void setRequestId(String requestId) {
		RequestId = requestId;
	}

	public String getBatchId() {
		return BatchId;
	}

	public void setBatchId(String batchId) {
		BatchId = batchId;
	}

	public String getDateInput() {
		return DateInput;
	}

	public void setDateInput(String dateInput) {
		DateInput = dateInput;
	}

	public CreateCustomerAccountLinkRequestDTO(String issueId) {
		this.IssueId = issueId;
	}
	
	public String getIssueId() {
		return IssueId;
	}

	public void setIssueId(String issueId) {
		IssueId = issueId;
	}
}
