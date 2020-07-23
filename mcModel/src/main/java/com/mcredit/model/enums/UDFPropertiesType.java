package com.mcredit.model.enums;

public enum UDFPropertiesType {

	NORMAL("N"),
	READONLY("R"),
	MANDATORY("M"),
	IDENTIFIER("I"),
	X_POS("X"),
	Y_POS("Y"),
	TAB_ORDER("O");

	private final String value;

	UDFPropertiesType(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

}
