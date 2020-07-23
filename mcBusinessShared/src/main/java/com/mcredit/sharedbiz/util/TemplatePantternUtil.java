package com.mcredit.sharedbiz.util;

import com.mcredit.model.dto.TemplatePanttern;

public class TemplatePantternUtil {

	public static TemplatePanttern toPanttern(String input) {
		String cleanInput = input.substring(1, input.length()-1);
		String[] splited = cleanInput.split("\\|");
		if (splited.length != 3 && splited.length != 2 ) return null;
		TemplatePanttern tp = new TemplatePanttern();
		tp.setType(splited[0]);
		tp.setName(splited[1]);
		if (splited.length == 3) tp.setFormat(splited[2]);
		tp.setOriginal(input);
		return tp;
	}
}
