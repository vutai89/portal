package com.mcredit.model.object.mobile.dto;

public class ReturnedCaseDTO {
	private Long documentId;
	
	private Long maxVersion;
	
	private String documentCode;

	public String getDocumentCode() {
		return documentCode;
	}

	public void setDocumentCode(String documentCode) {
		this.documentCode = documentCode;
	}

	public Long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	public Long getMaxVersion() {
		return maxVersion;
	}

	public void setMaxVersion(Long maxVersion) {
		this.maxVersion = maxVersion;
	}
}
