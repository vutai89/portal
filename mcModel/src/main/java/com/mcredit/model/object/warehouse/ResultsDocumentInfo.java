package com.mcredit.model.object.warehouse;

public class ResultsDocumentInfo {
	public Long errorId;
	public String errorCode;
	public String errorDECS;
	public Integer updateRequest;
	public String objectId;

	public Long getErrorId() {
		return errorId;
	}

	public void setErrorId(Long errorId) {
		this.errorId = errorId;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDECS() {
		return errorDECS;
	}

	public void setErrorDECS(String errorDECS) {
		this.errorDECS = errorDECS;
	}

	public Integer getUpdateRequest() {
		return updateRequest;
	}

	public void setUpdateRequest(Integer updateRequest) {
		this.updateRequest = updateRequest;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

}
