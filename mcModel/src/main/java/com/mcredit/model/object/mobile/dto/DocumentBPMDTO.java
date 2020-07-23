package com.mcredit.model.object.mobile.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentBPMDTO {
	private Long id;

	private String documentCode;

	private String documentName;

	private String inputDocUid;

	private String mapBpmVar;

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

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getInputDocUid() {
		return inputDocUid;
	}

	public void setInputDocUid(String inputDocUid) {
		this.inputDocUid = inputDocUid;
	}

	public String getMapBpmVar() {
		return mapBpmVar;
	}

	public void setMapBpmVar(String mapBpmVar) {
		this.mapBpmVar = mapBpmVar;
	}
}
