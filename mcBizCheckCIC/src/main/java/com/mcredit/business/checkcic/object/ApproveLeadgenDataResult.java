package com.mcredit.business.checkcic.object;

public class ApproveLeadgenDataResult {

	private Long numOfData;
	private Long approved;
	private Long disapproved;
	
	public ApproveLeadgenDataResult(Long numOfData, Long approved, Long disapproved) {
		this.numOfData = numOfData;
		this.approved = approved;
		this.disapproved = disapproved;
	}
	
	public Long getNumOfData() {
		return numOfData;
	}
	public void setNumOfData(Long numOfData) {
		this.numOfData = numOfData;
	}
	public Long getApproved() {
		return approved;
	}
	public void setApproved(Long approved) {
		this.approved = approved;
	}
	public Long getDisapproved() {
		return disapproved;
	}
	public void setDisapproved(Long disapproved) {
		this.disapproved = disapproved;
	}
	
	
	
}
