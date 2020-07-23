package com.mcredit.model.dto.cic;

import java.io.Serializable;

public class CICDetailDTO implements Serializable{
	
	private static final long serialVersionUID = 3611165463488579971L;
	
	private Long requestId;
	private String identifier;
	private String customerName;
	private String cicResult;
	private String description;
	private String cicImageLink;
	private String lastUpdateTime;
	private String status;
	private String numberOfRelationOrganize;
	
	public CICDetailDTO(Long requestId, String identifier, String customerName, String cicResult, String description,
			String cicImageLink, String lastUpdateTime, String status) {
		this.requestId = requestId;
		this.identifier = identifier;
		this.customerName = customerName;
		this.cicResult = cicResult;
		this.description = description;
		this.cicImageLink = cicImageLink;
		this.lastUpdateTime = lastUpdateTime;
		this.status = status;
	}

	public CICDetailDTO(String identifier, String customerName, String cicResult, String description,
			String cicImageLink, String lastUpdateTime, String status) {
		this(null, identifier, customerName, cicResult, description, cicImageLink, lastUpdateTime, status);
	}
	
	public CICDetailDTO(String identifier, String customerName) {
		this.identifier = identifier;
		this.customerName = customerName;
	}
	
	public CICDetailDTO(String identifier) {
		this.identifier = identifier;
	}
	
	public CICDetailDTO() {
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

	public void setStatus(String stauts) {
		this.status = stauts;
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public String getNumberOfRelationOrganize() {
		return numberOfRelationOrganize;
	}

	public void setNumberOfRelationOrganize(String numberOfRelationOrganize) {
		this.numberOfRelationOrganize = numberOfRelationOrganize;
	}

}
