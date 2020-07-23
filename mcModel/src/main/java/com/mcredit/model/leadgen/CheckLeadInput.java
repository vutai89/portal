package com.mcredit.model.leadgen;

import java.io.Serializable;

public class CheckLeadInput implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 9022754968790555294L;
	
	private String phoneNumber;
	private String nationalId;
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getNationalId() {
		return nationalId;
	}
	public void setNationalId(String nationalId) {
		this.nationalId = nationalId;
	}
}