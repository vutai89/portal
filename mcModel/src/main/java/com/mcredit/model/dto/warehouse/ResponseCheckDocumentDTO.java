package com.mcredit.model.dto.warehouse;
import java.util.List;

public class ResponseCheckDocumentDTO {
	
	private List<Integer> errorIds;
	
	private String errorSendMailMessage;

	public List<Integer> getErrorIds() {
		return errorIds;
	}

	public void setErrorIds(List<Integer> errorIds) {
		this.errorIds = errorIds;
	}

	public String getErrorSendMailMsg() {
		return errorSendMailMessage;
	}

	public void setErrorSendMailMsg(String errorSendMailMessage) {
		this.errorSendMailMessage = errorSendMailMessage;
	}
}
