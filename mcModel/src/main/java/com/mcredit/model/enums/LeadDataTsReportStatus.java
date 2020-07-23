package com.mcredit.model.enums;

public enum LeadDataTsReportStatus {
	PENDING("pending"),
	CONTACTED("contacted"),
	APPROVED("approved"),
	DISAPPROVED("disapproved"),
	CANCELLED("cancelled"),
	DISBURSED("disbursed");
	
	private final String value;

	LeadDataTsReportStatus(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

}