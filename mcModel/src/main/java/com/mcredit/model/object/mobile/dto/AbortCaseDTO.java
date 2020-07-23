package com.mcredit.model.object.mobile.dto;

public class AbortCaseDTO {
	private int abortProcess;
	private String abortProcessComment;
	private String abortProcessReason;
	
	public AbortCaseDTO(int abortProcess, String abortProcessReason,
			String abortProcessComment) {
		super();
		this.abortProcess = abortProcess;
		this.abortProcessComment = abortProcessComment;
		this.abortProcessReason = abortProcessReason;
	}

	public int getAbortProcess() {
		return abortProcess;
	}
	public void setAbortProcess(int abortProcess) {
		this.abortProcess = abortProcess;
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
}
