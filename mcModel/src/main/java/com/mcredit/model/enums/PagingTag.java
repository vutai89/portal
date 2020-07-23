package com.mcredit.model.enums;

public enum PagingTag {

	PAGESIZE_DEFAULT_10(10),
	PAGENUM_DEFAULT_1(1);

	private final Object value;

	PagingTag(Object value) {
		this.value = value;
	}

	public String stringValue() {
		return this.value.toString();
	}

	public Integer intValue() {
		return Integer.valueOf(this.value.toString());
	}
}
