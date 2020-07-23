package com.mcredit.model.enums;

public enum ExtendDateValue {
	EXTEND_DATE_VIEW_DOC("EXTEND_DATE_VIEW_DOC");

	private final Object value;

	ExtendDateValue(Object value) {
		this.value = value;
	}

	public String stringValue() {
		return this.value.toString();
	}

	public Long longValue() {
		return Long.valueOf(this.value.toString());
	}
}
