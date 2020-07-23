package com.mcredit.model.enums;

public enum AllocatedToTag {
	SUPERVISOR("S"),
	TEAM("T"),
	ALL_TEAM_LEAD(13051),
	ALL_TEAM_LEAD_EXCEPT(13052),
	TEAM_LEAD_OWNED(13058),
	ALL_TEAMMEM(13056),
	ALL_TEAMMEM_EXCEPT(13055),
	OWNER_TEAMLEAD(13053);
	
	private final Object value;

	AllocatedToTag(Object value) {
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
