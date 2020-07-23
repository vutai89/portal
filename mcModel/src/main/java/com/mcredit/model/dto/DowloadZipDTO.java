package com.mcredit.model.dto;

import java.io.Serializable;

public class DowloadZipDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String filePath;
	private String filename;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public DowloadZipDTO(String filePath, String filename) {
		super();
		this.filePath = filePath;
		this.filename = filename;
	}

}
