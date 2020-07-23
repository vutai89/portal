package com.mcredit.model.enums;

public enum AccountLinkType {
	
	CARD_ID("I"),
	CARD_NUMBER("C");
	
	private final String value;

	AccountLinkType(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
    
}
