package com.mcredit.model.dto.telesales;

import java.io.Serializable;
import java.util.Date;

public class UplDetailDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private Integer uplMasterId;
	private Integer uplSeq;
	private Integer imported;
	private Integer totalAllocated;
	private String uplFileName;
	private String serverFileName;
	private String status;
	private String recordStatus;
	private Date createdDate;
	private Date lastUpdatedDate;
	private String createdBy;
	private String lastUpdatedBy;
	private String rejectionReason;

	public UplDetailDTO() {

	}

	public UplDetailDTO(long id) {
         this.id=id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getUplMasterId() {
		return uplMasterId;
	}

	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	public void setUplMasterId(Integer uplMasterId) {
		this.uplMasterId = uplMasterId;
	}

	public Integer getUplSeq() {
		return uplSeq;
	}

	public void setUplSeq(Integer uplSeq) {
		this.uplSeq = uplSeq;
	}

	public Integer getTotalAllocated() {
		return totalAllocated;
	}

	public void setTotalAllocated(Integer totalAllocated) {
		this.totalAllocated = totalAllocated;
	}

	public String getUplFileName() {
		return uplFileName;
	}

	public void setUplFileName(String uplFileName) {
		this.uplFileName = uplFileName;
	}

	public String getServerFileName() {
		return serverFileName;
	}

	public void setServerFileName(String serverFileName) {
		this.serverFileName = serverFileName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getImported() {
		return imported;
	}

	public void setImported(Integer imported) {
		this.imported = imported;
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
}
