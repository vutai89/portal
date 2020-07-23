package com.mcredit.business.credit.dto;

public class CreditApplicationBPMDTO {
	private long id;
	private String bpmAppId;
	private Integer bpmAppNumber;
	private Long bpmId;
	private String createdBy;
	private Long creditAppId;
	private String lastUpdatedBy;
	private String recordStatus;

	public CreditApplicationBPMDTO(){
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getBpmAppId() {
		return bpmAppId;
	}
	public void setBpmAppId(String bpmAppId) {
		this.bpmAppId = bpmAppId;
	}
	public Integer getBpmAppNumber() {
		return bpmAppNumber;
	}
	public void setBpmAppNumber(Integer bpmAppNumber) {
		this.bpmAppNumber = bpmAppNumber;
	}
	public Long getBpmId() {
		return bpmId;
	}
	public void setBpmId(Long bpmId) {
		this.bpmId = bpmId;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Long getCreditAppId() {
		return creditAppId;
	}
	public void setCreditAppId(Long creditAppId) {
		this.creditAppId = creditAppId;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public String getRecordStatus() {
		return recordStatus;
	}
	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}
}
