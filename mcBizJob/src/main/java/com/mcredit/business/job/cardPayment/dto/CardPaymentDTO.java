package com.mcredit.business.job.cardPayment.dto;

import com.mcredit.data.common.entity.MessageLog;

public class CardPaymentDTO {
	private MessageLog messageLog; 
	
	public MessageLog getMessageLog() {
		return messageLog;
	}

	public void setMessageLog(MessageLog messageLog) {
		this.messageLog = messageLog;
	}
}
