package com.mcredit.model.object.warehouse;

public class WHAllocationDocumnetInput {
	public Long UserId;
	public Long documentId;

	public Long getUserId() {
		return UserId;
	}

	public void setUserId(Long userId) {
		UserId = userId;
	}

	public Long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

}
