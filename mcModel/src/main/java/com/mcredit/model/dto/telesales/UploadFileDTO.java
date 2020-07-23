package com.mcredit.model.dto.telesales;

import java.io.InputStream;
import java.io.Serializable;

public class UploadFileDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String userFileName;
	private InputStream fileContent;
	private String uplCode;
	private String uplType;
	private Long ownerId;
	private String mbMis;

	public String getMbMis() {
		return mbMis;
	}

	public void setMbMis(String mbMis) {
		this.mbMis = mbMis;
	}

	public String getUplType() {
		return uplType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public void setUplType(String uplType) {
		this.uplType = uplType;
	}

	public String getUserFileName() {
		return userFileName;
	}

	public void setUserFileName(String userFileName) {
		this.userFileName = userFileName;
	}

	public InputStream getFileContent() {
		return fileContent;
	}
	
	public void setFileContent(InputStream fileContent) {
		this.fileContent = fileContent;
	}

	public String getUplCode() {
		return uplCode;
	}
	
	public void setUplCode(String uplCode) {
		this.uplCode = uplCode;
	}
}
