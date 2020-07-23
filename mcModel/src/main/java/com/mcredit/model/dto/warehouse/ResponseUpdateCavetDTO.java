package com.mcredit.model.dto.warehouse;

import java.util.List;

public class ResponseUpdateCavetDTO {

    private List<Long> cavetIdLst;
    private Long WhDocumentId;
    private List<Long> errorIds;
    private String errorSendMailMsg;

    public List<Long> getCavetIdLst() {
        return cavetIdLst;
    }

    public void setCavetIdLst(List<Long> cavetIdLst) {
        this.cavetIdLst = cavetIdLst;
    }

    public List<Long> getErrorIds() {
        return errorIds;
    }

    public void setErrorIds(List<Long> errorIds) {
        this.errorIds = errorIds;
    }

    public Long getWhDocumentId() {
        return WhDocumentId;
    }

    public void setWhDocumentId(Long whDocumentId) {
        WhDocumentId = whDocumentId;
    }

	public String getErrorSendMailMsg() {
		return errorSendMailMsg;
	}

	public void setErrorSendMailMsg(String errorSendMailMsg) {
		this.errorSendMailMsg = errorSendMailMsg;
	}
}
