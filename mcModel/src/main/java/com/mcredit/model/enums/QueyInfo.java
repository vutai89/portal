package com.mcredit.model.enums;

public enum QueyInfo {
	
	CREDIT_CONTRACT("CREDIT"),
	PAYMENT_ON_WEB("PAYMENT");
	
	private final String value;

	QueyInfo(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}
