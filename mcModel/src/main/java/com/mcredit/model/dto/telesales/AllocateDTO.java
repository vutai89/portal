package com.mcredit.model.dto.telesales;

public class AllocateDTO {
	private Long id ;
	private String fromSource;
	private String uplCode;
	private String uplType;
	private String uplTypeDescription;
	private Long ownerId;
	private String ownerName;
	private Long teamId;
	private String ownerTeamLead;
	private Integer imported;
	private Integer totalUnallocated;
	private Long allocationMasterId;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFromSource() {
		return fromSource;
	}

	public void setFromSource(String fromSource) {
		this.fromSource = fromSource;
	}

	public String getUplCode() {
		return uplCode;
	}

	public void setUplCode(String uplCode) {
		this.uplCode = uplCode;
	}

	public String getUplType() {
		return uplType;
	}

	public void setUplType(String uplType) {
		this.uplType = uplType;
	}
	
	public String getUplTypeDescription() {
		return uplTypeDescription;
	}

	public void setUplTypeDescription(String uplTypeDescription) {
		this.uplTypeDescription = uplTypeDescription;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getOwnerTeamLead() {
		return ownerTeamLead;
	}

	public void setOwnerTeamLead(String ownerTeamLead) {
		this.ownerTeamLead = ownerTeamLead;
	}

	public Integer getImported() {
		return imported;
	}

	public void setImported(Integer imported) {
		this.imported = imported;
	}

	public Integer getTotalUnallocated() {
		return totalUnallocated;
	}

	public void setTotalUnallocated(Integer totalUnallocated) {
		this.totalUnallocated = totalUnallocated;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getTeamId() {
		return teamId;
	}

	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}

	public Long getAllocationMasterId() {
		return allocationMasterId;
	}

	public void setAllocationMasterId(Long allocationMasterId) {
		this.allocationMasterId = allocationMasterId;
	}
	
}
