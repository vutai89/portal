package com.mcredit.model.dto.warehouse;

public class ApproveBorrowCavetDTO {
	
	private Long id;
	private String reasonReject;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getReasonReject() {
		return reasonReject;
	}
	public void setReasonReject(String reasonReject) {
		this.reasonReject = reasonReject;
	}

}
