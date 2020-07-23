package com.mcredit.model.dto.telesales;

public class ReAssignDTO {

	private Long oldObjectId;
	private Long newObjectId;
	private Long wipNumber;
	private Long freshNumber;

	public Long getOldObjectId() {
		return oldObjectId;
	}

	public void setOldObjectId(Long oldObjectId) {
		this.oldObjectId = oldObjectId;
	}

	public Long getNewObjectId() {
		return newObjectId;
	}

	public void setNewObjectId(Long newObjectId) {
		this.newObjectId = newObjectId;
	}

	public Long getWipNumber() {
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
	}

}
