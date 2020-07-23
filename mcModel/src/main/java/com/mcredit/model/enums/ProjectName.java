package com.mcredit.model.enums;

public enum ProjectName {
	mcService("mcService"),
	mcMobileService("mcMobileService"),
	mcLOSService("mcLOSService"),
	mcWebSocket("mcWebSocket")
	;
	
	private String value;
	
	ProjectName(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}
