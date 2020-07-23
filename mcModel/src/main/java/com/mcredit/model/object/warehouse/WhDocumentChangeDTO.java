package com.mcredit.model.object.warehouse;

import java.io.Serializable;

public class WhDocumentChangeDTO implements Serializable {
	private static final long serialVersionUID = -507077586807618532L;

	private Long id;
	private Long whDocId;
	private String createdBy;
	private String createdDate;
	private Long errorType;
	private String errorCode;
	private String errorName;
	private Integer updateRequest;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getWhDocId() {
		return whDocId;
	}

	public void setWhDocId(Long whDocId) {
		this.whDocId = whDocId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Long getErrorType() {
		return errorType;
	}

	public void setErrorType(Long errorType) {
		this.errorType = errorType;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorName() {
		return errorName;
	}

	public void setErrorName(String errorName) {
		this.errorName = errorName;
	}

	public Integer getUpdateRequest() {
		return updateRequest;
	}

	public void setUpdateRequest(Integer updateRequest) {
		this.updateRequest = updateRequest;
	}

}
