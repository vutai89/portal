package com.mcredit.model.object.mobile.dto;

public class DocumentTypeDTO {
	private Long id;
	private String documentCode;
	private String docTypeVn;
	private String subFolder;

	public DocumentTypeDTO(Long id, String documentCode, String docTypeVn,
			String subFolder) {
		super();
		this.id = id;
		this.documentCode = documentCode;
		this.docTypeVn = docTypeVn;
		this.subFolder = subFolder;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDocumentCode() {
		return documentCode;
	}
	public void setDocumentCode(String documentCode) {
		this.documentCode = documentCode;
	}
	public String getDocTypeVn() {
		return docTypeVn;
	}
	public void setDocTypeVn(String docTypeVn) {
		this.docTypeVn = docTypeVn;
	}
	public String getSubFolder() {
		return subFolder;
	}
	public void setSubFolder(String subFolder) {
		this.subFolder = subFolder;
	}

}
