package com.mcredit.model.object.mobile;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UplCreditAppFilesDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6887928862144419618L;

	private Long id;

	private String recordStatus;

	private String createdBy;

	private Date createdDate;

	private String lastUpdatedBy;

	private Date lastUpdatedDate;

	private Long uplCreditAppId;

	private Long documentId;

	private Long documentSeq;

	private Long version;

	private String fileName;

	private String fileType;

	private String filePathServer;

	private String mimeType;

	private Long groupId;

	private String documentCode;

	public String getDocumentCode() {
		return documentCode;
	}

	public void setDocumentCode(String documentCode) {
		this.documentCode = documentCode;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
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

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public Long getUplCreditAppId() {
		return uplCreditAppId;
	}

	public void setUplCreditAppId(Long uplCreditAppId) {
		this.uplCreditAppId = uplCreditAppId;
	}

	public Long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	public Long getDocumentSeq() {
		return documentSeq;
	}

	public void setDocumentSeq(Long documentSeq) {
		this.documentSeq = documentSeq;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFilePathServer() {
		return filePathServer;
	}

	public void setFilePathServer(String filePathServer) {
		this.filePathServer = filePathServer;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private String status;
}
