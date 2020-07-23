package com.mcredit.model.leadgen;

import java.io.Serializable;

public class UploadDataId implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7375758917435900367L;
	
	private Long uploadMasterId;
	private Long uploadDetailId;
	
	public Long getUploadMasterId() {
		return uploadMasterId;
	}
	public void setUploadMasterId(Long uploadMasterId) {
		this.uploadMasterId = uploadMasterId;
	}
	public Long getUploadDetailId() {
		return uploadDetailId;
	}
	public void setUploadDetailId(Long uploadDetailId) {
		this.uploadDetailId = uploadDetailId;
	}
}