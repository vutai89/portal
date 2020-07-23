package com.mcredit.model.enums;

public enum TeamTag {
	SALE("S"),
	SYSTEM_ADMIN("A"),
	VERIFY_TEAM("P"),
	OPERATOR_TEAM("O"),
	THREERD_PARTY_TEAM("3"),
	TELESALE_GROUP("T"),
	SALE_ADMIN_GROUP("A"),
	COURIER_PROUP("C"),
	TEAM_GROUP_TELESALE("T");
	
	private final String value;

	TeamTag(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}
