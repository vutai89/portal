package com.mcredit.model.dto.telesales;

import java.util.Date;

public class AllocationMasterDTO {
	private Long id;

	private Long allocatedNumber;

	private Long allocatedSeq;

	private Long allocatedTo;

	private String allocatedType;

	private String createdBy;

	private Date createdDate;

	private String lastUpdatedBy;

	private Date lastUpdatedDate;

	private String recordStatus;

	private Long relatedAllocation;

	private Long uplMasterId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAllocatedNumber() {
		return allocatedNumber;
	}

	public void setAllocatedNumber(Long allocatedNumber) {
		this.allocatedNumber = allocatedNumber;
	}

	public Long getAllocatedSeq() {
		return allocatedSeq;
	}

	public void setAllocatedSeq(Long allocatedSeq) {
		this.allocatedSeq = allocatedSeq;
	}

	public Long getAllocatedTo() {
		return allocatedTo;
	}

	public void setAllocatedTo(Long allocatedTo) {
		this.allocatedTo = allocatedTo;
	}

	public String getAllocatedType() {
		return allocatedType;
	}

	public void setAllocatedType(String allocatedType) {
		this.allocatedType = allocatedType;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Long getRelatedAllocation() {
		return relatedAllocation;
	}

	public void setRelatedAllocation(Long relatedAllocation) {
		this.relatedAllocation = relatedAllocation;
	}

	public Long getUplMasterId() {
		return uplMasterId;
	}

	public void setUplMasterId(Long uplMasterId) {
		this.uplMasterId = uplMasterId;
	}

}
