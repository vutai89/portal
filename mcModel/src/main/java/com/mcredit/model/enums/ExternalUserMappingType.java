package com.mcredit.model.enums;

public enum ExternalUserMappingType {
	
	NORMAL(1L),
	THIENTU(2L),
	TOANCAU(3L)
	;
	
	private final Object value;
	
	ExternalUserMappingType(Object value) {
		this.value = value;
	}

	public String value() {
		return this.value.toString();
	}
	
	public Long longValue() {
		return (Long)this.value;
	}
}
