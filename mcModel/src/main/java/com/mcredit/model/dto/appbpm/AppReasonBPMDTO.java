package com.mcredit.model.dto.appbpm;

/**
 * @author huyendtt.ho
 *
 */
public class AppReasonBPMDTO {

	private String appId;
	private String appNumber;
	private String reasonCode;
	private String reasonDetailCode;
	private String createdBy;
	private String createdDate;
	private String historyIndex;
	private String fromUser;
	private String toUser;
	private String action;
	private String comment;
	private String currentStatus;

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

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getReasonDetailCode() {
		return reasonDetailCode;
	}

	public void setReasonDetailCode(String reasonDetailCode) {
		this.reasonDetailCode = reasonDetailCode;
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

	public String getHistoryIndex() {
		return historyIndex;
	}

	public void setHistoryIndex(String historyIndex) {
		this.historyIndex = historyIndex;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public AppReasonBPMDTO(String appId, String appNumber, String reasonCode, String reasonDetailCode,
			String createdBy, String createdDate, String historyIndex, String fromUser, String toUser, String action,
			String comment, String currentStatus) {
		super();
		this.appId = appId;
		this.appNumber = appNumber;
		this.reasonCode = reasonCode;
		this.reasonDetailCode = reasonDetailCode;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.historyIndex = historyIndex;
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.action = action;
		this.comment = comment;
		this.currentStatus = currentStatus;
	}

	public AppReasonBPMDTO() {
		super();
	}
}
