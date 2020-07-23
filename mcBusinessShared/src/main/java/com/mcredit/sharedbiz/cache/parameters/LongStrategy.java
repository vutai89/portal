package com.mcredit.sharedbiz.cache.parameters;

public class LongStrategy implements IDataTypeStrategy<Long> {

	@Override
	public Long getValue(String value) throws Exception {
		return Long.parseLong(value.trim());
	}

	@Override
	public Long getDefaultValue() {
		return 0L;
	}

}
