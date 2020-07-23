package com.mcredit.util;

public class NumericUtil {

	public static boolean isNullOrZero(Integer input) {
		return ((input == null) || (input == 0)) ? true : false;
	}

	public static boolean isNullOrZero(Long input) {
		return ((input == null) || (input == 0)) ? true : false;
	}

	public static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
