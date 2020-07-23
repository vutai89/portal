package com.mcredit.sharedbiz.cache.parameters;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateStrategy implements IDataTypeStrategy<Date> {

	@Override
	public Date getValue(String value) throws Exception {
		return new SimpleDateFormat("yyyyMMdd").parse(value);
	}

	@Override
	public Date getDefaultValue() {
		return null;
	}

}
