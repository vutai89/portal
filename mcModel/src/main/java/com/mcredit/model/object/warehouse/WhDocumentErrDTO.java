package com.mcredit.model.object.warehouse;

import java.io.Serializable;
import java.util.Date;

public class WhDocumentErrDTO implements Serializable {

	private static final long serialVersionUID = 4442945280853197487L;

	private Long id;

	private Long whDocId;

	private String createdBy;

	private Date createdDate;

	private Long errType;

	private Integer updateRequest;

	private String errorCode;
	private String errorName;

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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getErrType() {
		return errType;
	}

	public void setErrType(Long errType) {
		this.errType = errType;
	}


	public Integer getUpdateRequest() {
		return updateRequest;
	}

	public void setUpdateRequest(Integer updateRequest) {
		this.updateRequest = updateRequest;
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

}
