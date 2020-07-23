package com.mcredit.model.object;

import java.util.Date;

public class CreditApplicationSignatureOBJ {

	private Long id;
	private String createdBy;
	private Date createdDate;
	private String creditAppId;
	private String recordStatus;
	private Long version;
	private String mcContractNumber;
	private String signature;
	private String signatureContent;
	private String scannedBy;
	private Date scannedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreditAppId() {
		return creditAppId;
	}

	public void setCreditAppId(String creditAppId) {
		this.creditAppId = creditAppId;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getMcContractNumber() {
		return mcContractNumber;
	}

	public void setMcContractNumber(String mcContractNumber) {
		this.mcContractNumber = mcContractNumber;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSignatureContent() {
		return signatureContent;
	}

	public void setSignatureContent(String signatureContent) {
		this.signatureContent = signatureContent;
	}

	public String getScannedBy() {
		return scannedBy;
	}

	public void setScannedBy(String scannedBy) {
		this.scannedBy = scannedBy;
	}

	public Date getScannedAt() {
		return scannedAt;
	}

	public void setScannedAt(Date scannedAt) {
		this.scannedAt = scannedAt;
	}

}
