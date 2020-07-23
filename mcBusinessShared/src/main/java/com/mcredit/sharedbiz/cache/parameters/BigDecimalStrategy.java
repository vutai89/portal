package com.mcredit.sharedbiz.cache.parameters;

import java.math.BigDecimal;

public class BigDecimalStrategy implements IDataTypeStrategy<BigDecimal> {

	@Override
	public BigDecimal getValue(String value) throws Exception{
		return new BigDecimal(value.trim());
	}

	@Override
	public BigDecimal getDefaultValue() {
		return new BigDecimal("0");
	}

}
