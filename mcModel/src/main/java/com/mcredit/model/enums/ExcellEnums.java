package com.mcredit.model.enums;

public enum ExcellEnums {

	EXCEL_FROM_SOURCE("MIS");

	private final Object value;

	ExcellEnums(Object value) {
		this.value = value;
	}

	public String stringValue() {
		return this.value.toString();
	}

	public Integer intValue() {
		return Integer.valueOf(this.value.toString());
	}
}
