package com.mcredit.business.checkcic.object;

public class IdentityInfo {

	private String citizenID;
	private String citizenIdOld;
	private String militaryId;
	private String customerName;
	private String relationshipWithApplicant;
	private String spouseIDNumber;
	private String spouseName;
	
	public String getCitizenID() {
		return citizenID;
	}
	
	public void setCitizenID(String citizenID) {
		this.citizenID = citizenID;
	}
	
	public String getCitizenIdOld() {
		return citizenIdOld;
	}
	
	public void setCitizenIdOld(String citizenIdOld) {
		this.citizenIdOld = citizenIdOld;
	}
	
	public String getMilitaryId() {
		return militaryId;
	}
	
	public void setMilitaryId(String militaryId) {
		this.militaryId = militaryId;
	}
	
	public String getCustomerName() {
		return customerName;
	}
	
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getRelationshipWithApplicant() {
		return relationshipWithApplicant;
	}

	public void setRelationshipWithApplicant(String relationshipWithApplicant) {
		this.relationshipWithApplicant = relationshipWithApplicant;
	}

	public String getSpouseIDNumber() {
		return spouseIDNumber;
	}

	public void setSpouseIDNumber(String spouseIDNumber) {
		this.spouseIDNumber = spouseIDNumber;
	}

	public String getSpouseName() {
		return spouseName;
	}

	public void setSpouseName(String spouseName) {
		this.spouseName = spouseName;
	}
	
}
