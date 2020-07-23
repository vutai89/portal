package com.mcredit.model.enums;

import java.math.BigDecimal;

public enum FeeAmount {
	FREE_FEE_AMOUNT("0");

	private Object value;

	FeeAmount(Object value) {
		this.value = value;
	}

	public String stringValue() {
		return this.value.toString();
	}

	public BigDecimal bigDecimalValue() {
		return new BigDecimal(this.value.toString());
	}

	public Long longValue() {
		return (Long) this.value;
	}

	public int intValue() {
		return (Integer) this.value;
	}
}
