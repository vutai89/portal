package com.mcredit.model.object.mobile.dto;

import java.util.Date;

public class PdfDTO {
	private Long id;
	private Date createdDate;
	private String remotePathServer;
	private String fileName;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getRemotePathServer() {
		return remotePathServer;
	}
	
	public void setRemotePathServer(String remotePathServer) {
		this.remotePathServer = remotePathServer;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
