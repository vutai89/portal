package com.mcredit.sharedbiz.cache.parameters;

import java.sql.Time;
import java.text.SimpleDateFormat;

public class TimeStrategy implements IDataTypeStrategy<Time> {

	@Override
	public Time getValue(String value) throws Exception {
		return new Time(new SimpleDateFormat("HH:mm:ss").parse(value).getTime());
	}

	@Override
	public Time getDefaultValue() {
		return null;
	}

}
