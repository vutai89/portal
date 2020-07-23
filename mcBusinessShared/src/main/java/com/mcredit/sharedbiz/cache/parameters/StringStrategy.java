package com.mcredit.sharedbiz.cache.parameters;

public class StringStrategy implements IDataTypeStrategy<String> {

	@Override
	public String getValue(String value) throws Exception{
		return value.trim();
	}

	@Override
	public String getDefaultValue() {
		return "";
	}

}
