package com.mcredit.model.enums;

import java.math.BigDecimal;

public enum AmountFormat {
	PATTERN_AMOUNTFORMAT_1("##0,000.000"),
	PATTERN_AMOUNTFORMAT_2("##0,000");

	private Object value;

	AmountFormat(Object value) {
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