package com.mcredit.model.object.cancelCaseBPM;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VariableCaseBPM implements Serializable {
	private static final long serialVersionUID = -2666400980151367845L;

	@SerializedName("abortProcess")
	@Expose
	private String abortProcess;
	@SerializedName("abortProcessReason")
	@Expose
	private String abortProcessReason;
	@SerializedName("abortProcessComment")
	@Expose
	private String abortProcessComment;
	@SerializedName("appStatus")
	@Expose
	private String appStatus;

	public VariableCaseBPM(String abortProcess, String abortProcessReason, String abortProcessComment,String appStatus) {
		super();
		this.abortProcess = abortProcess;
		this.abortProcessReason = abortProcessReason;
		this.abortProcessComment = abortProcessComment;
		this.appStatus = appStatus;
	}

	public String getAbortProcess() {
		return abortProcess;
	}

	public void setAbortProcess(String abortProcess) {
		this.abortProcess = abortProcess;
	}

	public String getAbortProcessReason() {
		return abortProcessReason;
	}

	public void setAbortProcessReason(String abortProcessReason) {
		this.abortProcessReason = abortProcessReason;
	}

	public String getAbortProcessComment() {
		return abortProcessComment;
	}

	public void setAbortProcessComment(String abortProcessComment) {
		this.abortProcessComment = abortProcessComment;
	}

	public String getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(String appStatus) {
		this.appStatus = appStatus;
	}

}