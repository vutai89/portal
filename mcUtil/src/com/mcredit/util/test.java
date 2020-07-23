package com.mcredit.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.record.formula.functions.T;

public class test {

	public static void main(String[] args) throws Exception {

		System.out.print(StringUtils.isDigit("1000"));

	}

	static float floatMap(float x, float inMin, float inMax, float outMin, float outMax) {
		return (x - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
	}

	static List<?> hashMapToList(HashMap<String, Object> input ,Class<T> classOut) {
		
		T result = null;
		try {
			result = classOut.newInstance();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		
		if( result==null )
			return null;
		List<T> resoult = new ArrayList<>();

		for (Map.Entry<String, Object> entry : input.entrySet()) {
			resoult.add((T) entry.getValue());
		}

		return resoult;
	}
}
