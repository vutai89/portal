package com.mcredit.business.job.createCard.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mcredit.model.enums.T24OtherTag;

public class CreateCustomerAccountLinkItemsDTO {

	@SerializedName("IssueId")
	@Expose
	private String IssueId;

	@SerializedName("CardId")
	@Expose
	private Long CardId;

	@SerializedName("CardNumber")
	@Expose
	private String CardNumber;

	@SerializedName("IssueStatus")
	@Expose
	private String IssueStatus;

	@SerializedName("IssueDesc")
	@Expose
	private String IssueDesc;

	public String getIssueId() {
		return IssueId;
	}

	public void setIssueId(String issueId) {
		IssueId = issueId;
	}

	public Long getCardId() {
		return CardId;
	}

	public void setCardId(Long cardId) {
		CardId = cardId;
	}

	public String getCardNumber() {
		return CardNumber;
	}

	public void setCardNumber(String cardNumber) {
		CardNumber = cardNumber;
	}

	public String getIssueStatus() {
		return IssueStatus;
	}

	public Boolean isIssueStatus() {
		return T24OtherTag.WAY4_ISSUE_STATUS_SUCCESS.value().contains(IssueStatus);
	}

	public void setIssueStatus(String issueStatus) {
		IssueStatus = issueStatus;
	}

	public String getIssueDesc() {
		return IssueDesc;
	}

	public void setIssueDesc(String issueDesc) {
		IssueDesc = issueDesc;
	}
}
