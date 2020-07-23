package com.mcredit.util;

import java.math.BigDecimal;

import com.mcredit.model.enums.RuleParameterType;

public class RuleUtils {

	public static String convert2VariableName(String inputName, String paramType) {
		String[] temp = inputName.split(":");
		String varName = "";
		if(RuleParameterType.FORMULA.value().equals(paramType)) {
			varName = temp[2];
		} else if(RuleParameterType.CONSTANT.value().equals(paramType)) {
			varName = "";
		} else {
			String pName = temp[2].substring(3);
			varName = pName.substring(0, 1).toLowerCase() + pName.substring(1);
		}
		
		return varName;
	}
	
	public static String convert2GetMethodName(String inputName, String paramType) {
		String[] temp = inputName.split(":");
		String methodName = "";
		if(RuleParameterType.FORMULA.value().equals(paramType)) {
			methodName = temp[1];
		} else if(RuleParameterType.CONSTANT.value().equals(paramType)) {
			methodName = "";
		} else {
			methodName = temp[2];
		}
		
		return methodName;
	}
	
	public static String convert2String(Object obj) {
		String tmpValue = null;
		if(obj instanceof BigDecimal) {
			tmpValue = ((BigDecimal) obj).toString();
		} else if(obj instanceof Integer) {
			tmpValue = ((Integer) obj).toString();
		} else {
			tmpValue = (String) obj;
		}
		
		return tmpValue;
	}
}
