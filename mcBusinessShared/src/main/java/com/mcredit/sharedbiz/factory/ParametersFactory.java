package com.mcredit.sharedbiz.factory;

import com.mcredit.model.enums.ParametersDataType;
import com.mcredit.sharedbiz.cache.parameters.IDataTypeStrategy;
import com.mcredit.sharedbiz.cache.parameters.BigDecimalStrategy;
import com.mcredit.sharedbiz.cache.parameters.DateStrategy;
import com.mcredit.sharedbiz.cache.parameters.IntegerStrategy;
import com.mcredit.sharedbiz.cache.parameters.LongStrategy;
import com.mcredit.sharedbiz.cache.parameters.StringStrategy;
import com.mcredit.sharedbiz.cache.parameters.TimeStrategy;

public class ParametersFactory {

	public static IDataTypeStrategy<?> createDataTypeStrategy(String parametersType) {
		
		if(parametersType.equalsIgnoreCase(ParametersDataType.STRING.value()))
			return new StringStrategy();
		
		else if(parametersType.equalsIgnoreCase(ParametersDataType.INTERGER.value()))
			return new IntegerStrategy();
		
		else if(parametersType.equalsIgnoreCase(ParametersDataType.LONG.value()))
			return new LongStrategy();
		
		else if(parametersType.equalsIgnoreCase(ParametersDataType.BIGDECIMAL.value()))
			return new BigDecimalStrategy();
		
		else if(parametersType.equalsIgnoreCase(ParametersDataType.DATE.value()))
			return new DateStrategy();
		
		else if(parametersType.equalsIgnoreCase(ParametersDataType.TIME.value()))
			return new TimeStrategy();
		
		else
			return new StringStrategy();
	}

}
