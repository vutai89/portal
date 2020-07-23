package com.mcredit.model.dto.warehouse;

import java.io.Serializable;
import java.util.List;

import com.mcredit.model.object.warehouse.Document;

public class DocumentsDTO implements Serializable {
	
	private static final long serialVersionUID = -6373394785437408281L;
	private String codeValue1;
	private List<Document> documents;

	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

	public String getCodeValue1() {
		return codeValue1;
	}

	public void setCodeValue1(String codeValue1) {
		this.codeValue1 = codeValue1;
	}

}
