package com.mcredit.model.dto.telesales;

public class CallResultDTO {
	private Long id;

	private Long allocationDetailId;

	private Long custProspectId;

	private Integer callTimes;

	private String callStatus;
	private String callStatusDes;

	private String callResult;
	private String callResultDes;

	private String nextAction;
	private String nextActionDes;

	public String getCallStatusDes() {
		return callStatusDes;
	}

	public void setCallStatusDes(String callStatusDes) {
		this.callStatusDes = callStatusDes;
	}

	public String getCallResultDes() {
		return callResultDes;
	}

	public void setCallResultDes(String callResultDes) {
		this.callResultDes = callResultDes;
	}

	public String getNextActionDes() {
		return nextActionDes;
	}

	public void setNextActionDes(String nextActionDes) {
		this.nextActionDes = nextActionDes;
	}

	private String nextActionDate;// format:dd/MM/yyyy

	private String callDate;// format:dd/MM/yyyy

	private String note;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAllocationDetailId() {
		return allocationDetailId;
	}

	public void setAllocationDetailId(Long allocationDetailId) {
		this.allocationDetailId = allocationDetailId;
	}

	public Long getCustProspectId() {
		return custProspectId;
	}

	public void setCustProspectId(Long custProspectId) {
		this.custProspectId = custProspectId;
	}

	public Integer getCallTimes() {
		return callTimes;
	}

	public void setCallTimes(Integer callTimes) {
		this.callTimes = callTimes;
	}

	public String getCallStatus() {
		return callStatus;
	}

	public void setCallStatus(String callStatus) {
		this.callStatus = callStatus;
	}

	public String getCallResult() {
		return callResult;
	}

	public void setCallResult(String callResult) {
		this.callResult = callResult;
	}

	public String getNextAction() {
		return nextAction;
	}

	public void setNextAction(String nextAction) {
		this.nextAction = nextAction;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getNextActionDate() {
		return nextActionDate;
	}

	public void setNextActionDate(String nextActionDate) {
		this.nextActionDate = nextActionDate;
	}

	public String getCallDate() {
		return callDate;
	}

	public void setCallDate(String callDate) {
		this.callDate = callDate;
	}

}
