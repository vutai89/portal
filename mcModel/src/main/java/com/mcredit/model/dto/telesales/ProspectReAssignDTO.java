package com.mcredit.model.dto.telesales;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ProspectReAssignDTO {

	@JsonFormat(shape=JsonFormat.Shape.ARRAY)
	List<ProspectReAssignDTO> lstProspectReAssign;
	
	private Long oldObjectId;
	
	@JsonFormat(shape=JsonFormat.Shape.ARRAY)
	private List<Long> newObjectId;
	
	private Long allocatedNumber;
	private String prospectStatus;
	private Long uplMasterId;
	//private Long wipNumber;
	//private Long freshNumber;
	
	public Long getOldObjectId() {
		return oldObjectId;
	}
	public Long getUplMasterId() {
		return uplMasterId;
	}
	public void setUplMasterId(Long uplMasterId) {
		this.uplMasterId = uplMasterId;
	}
	public Long getAllocatedNumber() {
		return allocatedNumber;
	}
	public void setAllocatedNumber(Long allocatedNumber) {
		this.allocatedNumber = allocatedNumber;
	}
	public String getProspectStatus() {
		return prospectStatus;
	}
	public void setProspectStatus(String prospectStatus) {
		this.prospectStatus = prospectStatus;
	}
	public void setOldObjectId(Long oldObjectId) {
		this.oldObjectId = oldObjectId;
	}
	public List<Long> getNewObjectId() {
		return newObjectId;
	}
	public void setNewObjectId(List<Long> newObjectId) {
		this.newObjectId = newObjectId;
	}
	public List<ProspectReAssignDTO> getLstProspectReAssign() {
		return lstProspectReAssign;
	}
	public void setLstProspectReAssign(List<ProspectReAssignDTO> lstProspectReAssign) {
		this.lstProspectReAssign = lstProspectReAssign;
	}
	/*public Long getWipNumber() {
		return wipNumber;
	}
	public void setWipNumber(Long wipNumber) {
		this.wipNumber = wipNumber;
	}
	public Long getFreshNumber() {
		return freshNumber;
	}
	public void setFreshNumber(Long freshNumber) {
		this.freshNumber = freshNumber;
	}*/
}