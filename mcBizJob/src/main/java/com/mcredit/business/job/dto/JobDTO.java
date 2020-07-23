package com.mcredit.business.job.dto;

import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.model.enums.MsgStatus;

public class JobDTO {
	private String returnId; // Get Customer Code from T24 (Step 1)
	private MessageLog messageLog;
	private MsgStatus messageTaskStatus;
	public String getReturnId() {
		return returnId;
	}
	public void setReturnId(String returnId) {
		this.returnId = returnId;
	}
	public MessageLog getMessageLog() {
		return messageLog;
	}
	public void setMessageLog(MessageLog messageLog) {
		this.messageLog = messageLog;
	}
	public MsgStatus getMessageTaskStatus() {
		return messageTaskStatus;
	}
	public void setMessageTaskStatus(MsgStatus messageTaskStatus) {
		this.messageTaskStatus = messageTaskStatus;
	}

}
