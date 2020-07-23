package com.mcredit.model.dto.appbpm;

public class AppStatusBPMDTO {
	private String appId;
	private String appNumber;
	private String appStatus;
	private String businessLine;
	private String createdBy;
	private String createdDate;
	private String currentTask;
	private String finishDate;
	private String processId;
	private String processName;
	private String updateBy;
	private String updateDate;
	private String abortProcessComment;
	private String abortProcessReason;
	private String productGroup;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppNumber() {
		return appNumber;
	}

	public void setAppNumber(String appNumber) {
		this.appNumber = appNumber;
	}

	public String getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(String appStatus) {
		this.appStatus = appStatus;
	}

	public String getBusinessLine() {
		return businessLine;
	}

	public void setBusinessLine(String businessLine) {
		this.businessLine = businessLine;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getCurrentTask() {
		return currentTask;
	}

	public void setCurrentTask(String currentTask) {
		this.currentTask = currentTask;
	}

	public String getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getAbortProcessComment() {
		return abortProcessComment;
	}

	public void setAbortProcessComment(String abortProcessComment) {
		this.abortProcessComment = abortProcessComment;
	}

	public String getAbortProcessReason() {
		return abortProcessReason;
	}

	public void setAbortProcessReason(String abortProcessReason) {
		this.abortProcessReason = abortProcessReason;
	}

	public String getProductGroup() {
		return productGroup;
	}

	public void setProductGroup(String productGroup) {
		this.productGroup = productGroup;
	}

	public AppStatusBPMDTO(String appStatus, String businessLine, String currentTask, String finishDate,
			String processId, String processName, String updateBy, String updateDate, String abortProcessComment,
			String abortProcessReason, String productGroup) {
		super();
		this.appStatus = appStatus;
		this.businessLine = businessLine;
		this.currentTask = currentTask;
		this.finishDate = finishDate;
		this.processId = processId;
		this.processName = processName;
		this.updateBy = updateBy;
		this.updateDate = updateDate;
		this.abortProcessComment = abortProcessComment;
		this.abortProcessReason = abortProcessReason;
		this.productGroup = productGroup;
	}

	public AppStatusBPMDTO() {
		super();
	}

}
