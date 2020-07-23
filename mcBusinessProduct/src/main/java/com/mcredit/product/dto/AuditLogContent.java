package com.mcredit.product.dto;

public class AuditLogContent {

	public AuditLogContent() {
		// TODO Auto-generated constructor stub
	}

	public AuditLogContent(Integer channelSaleId,String categoryType, Long idUploadMater, String contentDataChange) {
		this.channelSaleId = channelSaleId;
		this.idUploadMater = idUploadMater;
		this.contentDataChange = contentDataChange;
		this.categoryType = categoryType;
	}
	
	public AuditLogContent(Integer channelSaleId,String categoryType,String contentDataChange, Long idUploadMater, String fileDir, String fileName) {
		this.channelSaleId = channelSaleId;
		this.idUploadMater = idUploadMater;
		this.contentDataChange = contentDataChange;
		this.categoryType = categoryType;
		this.fileDir = fileDir;
		this.fileName = fileName;
	}
	
	private String actionText;
	private String contentDataChange;
	private Long idUploadMater;
	private String fileUploadDir;
	private Integer channelSaleId;
	private String categoryType;
	private String fileDir;
	private String fileName;
	
	public String getActionText() {
		return actionText;
	}
	public void setActionText(String actionText) {
		this.actionText = actionText;
	}
	public String getContentDataChange() {
		return contentDataChange;
	}
	public void setContentDataChange(String contentDataChange) {
		this.contentDataChange = contentDataChange;
	}
	
	public Long getIdUploadMater() {
		return idUploadMater;
	}

	public void setIdUploadMater(Long idUploadMater) {
		this.idUploadMater = idUploadMater;
	}

	public String getFileUploadDir() {
		return fileUploadDir;
	}
	public void setFileUploadDir(String fileUploadDir) {
		this.fileUploadDir = fileUploadDir;
	}
	public Integer getChannelSaleId() {
		return channelSaleId;
	}
	public void setChannelSaleId(Integer channelSaleId) {
		this.channelSaleId = channelSaleId;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public String getFileDir() {
		return fileDir;
	}

	public void setFileDir(String fileDir) {
		this.fileDir = fileDir;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
