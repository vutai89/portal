package com.mcredit.model.leadgen;

import java.io.Serializable;

public class UploadCustomerDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8485258932521760219L;
	
	private Long id;
	private Long uplDetailId;
	private String responseCode;
	
	public UploadCustomerDTO() {
	}
	
	public UploadCustomerDTO(Long id, Long uplDetailId, String responseCode) {
		this.id = id;
		this.uplDetailId = uplDetailId;
		this.responseCode = responseCode;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUplDetailId() {
		return uplDetailId;
	}
	public void setUplDetailId(Long uplDetailId) {
		this.uplDetailId = uplDetailId;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
}