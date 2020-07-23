package com.mcredit.model.object.mobile.dto;

public class FcmIOSMessDataFormatDTO {
	private String fileUrl;
	
	public FcmIOSMessDataFormatDTO(String fileUrl) {
		super();
		this.fileUrl = fileUrl;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	
}
