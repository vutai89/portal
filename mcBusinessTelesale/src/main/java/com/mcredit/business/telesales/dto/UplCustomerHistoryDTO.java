package com.mcredit.business.telesales.dto;

import java.util.Date;

public class UplCustomerHistoryDTO {
	private Long id;
	
	private Date lastUpdatedDate;

	private Long uplCustomerId;

	private Long uplMasterId;
	
	private String refId;

	private String responseCode;
	
	private String message;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public Long getUplCustomerId() {
		return uplCustomerId;
	}

	public void setUplCustomerId(Long uplCustomerId) {
		this.uplCustomerId = uplCustomerId;
	}

	public Long getUplMasterId() {
		return uplMasterId;
	}

	public void setUplMasterId(Long uplMasterId) {
		this.uplMasterId = uplMasterId;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
