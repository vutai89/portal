package com.mcredit.business.job.createCard.dto;

import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.model.enums.MsgStatus;

public class CreateCardDTO {

	private String coreCustCode; // Get Customer Code from T24 (Step 1)
	private String cardId;
	private Long issueId; // Get issueId from MainJob after finish Step5 to start Step 6
	private MessageLog messageLog;
	private MsgStatus messageTaskStatus;

	public MsgStatus getMessageTaskStatus() {
		return messageTaskStatus;
	}

	public void setMessageTaskStatus(MsgStatus messageTaskStatus) {
		this.messageTaskStatus = messageTaskStatus;
	}

	public String getCoreCustCode() {
		return coreCustCode;
	}

	public void setCoreCustCode(String coreCustCode) {
		this.coreCustCode = coreCustCode;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public Long getIssueId() {
		return issueId;
	}

	public void setIssueId(Long issueId) {
		this.issueId = issueId;
	}

	public MessageLog getMessageLog() {
		return messageLog;
	}

	public void setMessageLog(MessageLog messageLog) {
		this.messageLog = messageLog;
	}
}
