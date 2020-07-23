package com.mcredit.model.object.warehouse;

public enum WareHouseEnum {
	SUB_DAYS_("DAYS_"), SUB_DAYS_BORROW_("DAYS_BR_"), CONTRACTCAVETTYPE_CONTRACT(1), CONTRACTCAVETTYPE_CAVET(2),
	INPUTCASE("inputcase");

	private final Object value;

	WareHouseEnum(Object value) {
		this.value = value;
	}

	public String stringValue() {
		return this.value.toString();
	}

	public Long longValue() {
		return Long.valueOf(this.value.toString());
	}
}
