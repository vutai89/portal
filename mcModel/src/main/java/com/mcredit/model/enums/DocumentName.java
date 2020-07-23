package com.mcredit.model.enums;

public enum DocumentName {
	CREDIT_CONTRACT("CreditContract"),
	CCCD("CCCD"),
	CMTND("CMTND"),
	CMTQD("CMTQ\u0110"),
	SHK("SHK"),
	HAKH("HAKH"),
	PTTKH("PTTKH");
	
	private final String value;

	DocumentName(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}
