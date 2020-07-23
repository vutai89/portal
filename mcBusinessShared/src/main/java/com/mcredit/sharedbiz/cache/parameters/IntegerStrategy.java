package com.mcredit.sharedbiz.cache.parameters;

public class IntegerStrategy implements IDataTypeStrategy<Integer> {

	@Override
	public Integer getValue(String value) throws Exception {
		return Integer.parseInt(value.trim());
	}

	@Override
	public Integer getDefaultValue() {
		return 0;
	}

}
