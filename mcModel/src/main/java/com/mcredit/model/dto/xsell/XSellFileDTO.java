package com.mcredit.model.dto.xsell;

import java.util.Date;

public class XSellFileDTO {

	private Long id;

	private String createdDate;
	
	private String userUpload;
	
	private String userApprove;

	private String fileName;

	private String uplCode;

	private Date validDateFrom;

	private Date validDateTo;

	private String storageFileUrl;

	private String status;

	private String rejectReason;

	private String dateApprove;

	private String status_app;

	public String getStatus_app() {
		return status_app;
	}

	public void setStatus_app(String status_app) {
		this.status_app = status_app;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUserUpload() {
		return userUpload;
	}

	public void setUserUpload(String userUpload) {
		this.userUpload = userUpload;
	}

	public String getUserApprove() {
		return userApprove;
	}

	public void setUserApprove(String userApprove) {
		this.userApprove = userApprove;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getUplCode() {
		return uplCode;
	}

	public void setUplCode(String uplCode) {
		this.uplCode = uplCode;
	}

	public String getStorageFileUrl() {
		return storageFileUrl;
	}

	public void setStorageFileUrl(String storageFileUrl) {
		this.storageFileUrl = storageFileUrl;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getDateApprove() {
		return dateApprove;
	}

	public void setDateApprove(String dateApprove) {
		this.dateApprove = dateApprove;
	}

	public Date getValidDateFrom() {
		return validDateFrom;
	}

	public void setValidDateFrom(Date validDateFrom) {
		this.validDateFrom = validDateFrom;
	}

	public Date getValidDateTo() {
		return validDateTo;
	}

	public void setValidDateTo(Date validDateTo) {
		this.validDateTo = validDateTo;
	}
}
