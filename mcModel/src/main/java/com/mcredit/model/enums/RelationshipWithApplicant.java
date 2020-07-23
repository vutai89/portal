package com.mcredit.model.enums;

public enum RelationshipWithApplicant {
	
	SPOUSE("1"),
	PARENTS("2"),
	SIBLINGS("3"),
	OTHER("other");
	
	private final String value;

	RelationshipWithApplicant(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
    
}
