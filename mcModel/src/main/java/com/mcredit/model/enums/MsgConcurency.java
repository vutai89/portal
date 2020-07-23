package com.mcredit.model.enums;

public enum MsgConcurency {
	
	VND("VND");
	
	private final String value;

	MsgConcurency(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
    
}
