package com.mcredit.model.object.warehouse;

public class LodgeDocumentData {
	
	private Long id;
	private String status;
	
	public LodgeDocumentData() {
	}
	
	public LodgeDocumentData(Long id, String status) {
		this.id = id;
		this.status = status;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
