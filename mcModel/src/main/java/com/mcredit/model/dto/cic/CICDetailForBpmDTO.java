package com.mcredit.model.dto.cic;

import java.io.Serializable;

public class CICDetailForBpmDTO implements Serializable{
	
	private static final long serialVersionUID = 3611165463488579971L;
	
	private String identifier;
	private String customerName;
	private String cicResult;
	private String description;
	private String cicImageLink;
	private String lastUpdateTime;
	private String status;
	private String cicExtensionLink;
	
	public CICDetailForBpmDTO(String identifier) {
		this.identifier = identifier;
	}

	public CICDetailForBpmDTO(String identifier, String customerName, String cicResult, String description,
			String cicImageLink, String lastUpdateTime, String status, String cicExtensionLink) {
		this.identifier = identifier;
		this.customerName = customerName;
		this.cicResult = cicResult;
		this.description = description;
		this.cicImageLink = cicImageLink;
		this.lastUpdateTime = lastUpdateTime;
		this.status = status;
		this.cicExtensionLink = cicExtensionLink;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCicResult() {
		return cicResult;
	}

	public void setCicResult(String cicResult) {
		this.cicResult = cicResult;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCicImageLink() {
		return cicImageLink;
	}

	public void setCicImageLink(String cicImageLink) {
		this.cicImageLink = cicImageLink;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCicExtensionLink() {
		return cicExtensionLink;
	}

	public void setCicExtensionLink(String cicExtensionLink) {
		this.cicExtensionLink = cicExtensionLink;
	}

}
