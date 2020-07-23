package com.mcredit.model.object.system;

import java.io.Serializable;
import java.util.ArrayList;

public class BPMDocumentFile implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String docName;
	private String docType;
	private ArrayList<String> docPageList;

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public ArrayList<String> getDocPageList() {
		return docPageList;
	}

	public void setDocPageList(ArrayList<String> docPageList) {
		this.docPageList = docPageList;
	}
}
