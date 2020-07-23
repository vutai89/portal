package com.mcredit.util;

public class IntUtil {
	public static Integer toId(Integer value) {
		return value == null || value == 0 ? 1 : value;
	}
}
