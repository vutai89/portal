package com.mcredit.model.enums;

public enum TelesaleTag {
	
	UPLDETAIL_STATUS_A("A"),
	UPLDETAIL_STATUS_S("S"),
	UPLDETAIL_STATUS_P("P"),
	UPLDETAIL_STATUS_N("N"),
	ALLOCATIONDETAIL_ALCTYPE_SP("ALCTYPE_SP");
	
	
	private final Object value;

	TelesaleTag(Object value) {
		this.value = value;
	}

	public String stringValue() {
		return this.value.toString();
	}
	public Long longValue() {
		return (Long)this.value;
	}
	
	public Integer intValue() {
		return (Integer)this.value;
	}
}
