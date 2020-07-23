package com.mcredit.model.object.mobile.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckListDTO {
	private Long groupId;

	private String groupName;

	private int mandatory;

	private int hasAlternate;

	List<DocumentBPMDTO> documents;

	List<Long> alternateGroups;

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getMandatory() {
		return mandatory;
	}

	public void setMandatory(int mandatory) {
		this.mandatory = mandatory;
	}

	public int getHasAlternate() {
		return hasAlternate;
	}

	public void setHasAlternate(int hasAlternate) {
		this.hasAlternate = hasAlternate;
	}

	public List<DocumentBPMDTO> getDocuments() {
		return documents;
	}

	public void setDocuments(List<DocumentBPMDTO> documents) {
		this.documents = documents;
	}

	public List<Long> getAlternateGroups() {
		return alternateGroups;
	}

	public void setAlternateGroups(List<Long> alternateGroups) {
		this.alternateGroups = alternateGroups;
	}
}
