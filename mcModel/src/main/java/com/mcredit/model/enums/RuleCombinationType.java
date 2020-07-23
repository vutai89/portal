package com.mcredit.model.enums;

public enum RuleCombinationType {

	QUERY("Q"),
	DROOL_FILE("R"),
	DROP_LIST("D"),
	OUTPUT_DIRECT("O");

	private final String value;

	RuleCombinationType(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

}
