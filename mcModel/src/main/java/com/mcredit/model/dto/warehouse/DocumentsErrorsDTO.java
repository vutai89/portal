package com.mcredit.model.dto.warehouse;

import java.io.Serializable;
import java.util.List;

public class DocumentsErrorsDTO implements Serializable {

	private static final long serialVersionUID = 2332149376549282097L;

	private boolean save;
	private Long whDocumentId;
	private List<ErrorType> errorTypes;

	public Long getWhDocumentId() {
		return whDocumentId;
	}

	public void setWhDocumentId(Long whDocumentId) {
		this.whDocumentId = whDocumentId;
	}

	public List<ErrorType> getErrorTypes() {
		return errorTypes;
	}

	public void setErrorTypes(List<ErrorType> errorTypes) {
		this.errorTypes = errorTypes;
	}

	public boolean isSave() {
		return save;
	}

	public void setSave(boolean isSave) {
		this.save = isSave;
	}
	
	
}
