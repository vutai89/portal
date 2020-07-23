package com.mcredit.model.dto.warehouse;

public class CompareInfoDTO {
	private String scanDoc;
	private String dbDoc;
	private String result;

	public String getScanDoc() {
		return scanDoc;
	}

	public void setScanDoc(String scanDoc) {
		this.scanDoc = scanDoc;
	}

	public String getDbDoc() {
		return dbDoc;
	}

	public void setDbDoc(String dbDoc) {
		this.dbDoc = dbDoc;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public CompareInfoDTO(String scanDoc, String dbDoc, String result) {
		super();
		this.scanDoc = scanDoc;
		this.dbDoc = dbDoc;
		this.result = result;
	}

	public CompareInfoDTO() {
		super();
	}

}
