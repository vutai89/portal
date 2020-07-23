package com.mcredit.model.dto.telesales;

import java.io.Serializable;
import java.util.Date;

import com.mcredit.model.dto.CodeTableDTO;

public class UplMasterDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private String uplFormat;
	private Integer fromSource;
	private String uplCode;
	private CodeTableDTO uplType;
	private Integer ownerId;
	private Integer imported;
	private Integer totalAllocated;
	private String createdBy;
	private Date createdDate;
	private String lastUpdatedBy;
	private Date lastUpdatedDate;
	private String recordStatus;

	private String ownerName;
	private Long teamLeadId;
	private String ownerTeamLead;

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Long getTeamLeadId() {
		return teamLeadId;
	}

	public void setTeamLeadId(Long teamLeadId) {
		this.teamLeadId = teamLeadId;
	}

	public String getOwnerTeamLead() {
		return ownerTeamLead;
	}

	public void setOwnerTeamLead(String ownerTeamLead) {
		this.ownerTeamLead = ownerTeamLead;
	}

	public CodeTableDTO getUplType() {
		return uplType;
	}

	public void setUplType(CodeTableDTO uplType) {
		this.uplType = uplType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUplFormat() {
		return uplFormat;
	}

	public void setUplFormat(String uplFormat) {
		this.uplFormat = uplFormat;
	}

	public Integer getFromSource() {
		return fromSource;
	}

	public void setFromSource(Integer fromSource) {
		this.fromSource = fromSource;
	}

	public String getUplCode() {
		return uplCode;
	}

	public void setUplCode(String uplCode) {
		this.uplCode = uplCode;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	public Integer getImported() {
		return imported;
	}

	public void setImported(Integer imported) {
		this.imported = imported;
	}

	public Integer getTotalAllocated() {
		return totalAllocated;
	}

	public void setTotalAllocated(Integer totalAllocated) {
		this.totalAllocated = totalAllocated;
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
