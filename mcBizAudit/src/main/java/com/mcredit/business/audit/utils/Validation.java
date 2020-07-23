package com.mcredit.business.audit.utils;

import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.model.dto.audit.AuditCommandDTO;
import com.mcredit.sharedbiz.validation.ValidationException;

public class Validation {
	public static void validateExcel(String date, AuditCommandDTO audit) throws ValidationException {
		date = date.toLowerCase().trim();
		if (date.startsWith(Labels.getString("label.audit.excel.startString"))) {
			String startDate = date.substring(date.indexOf(Labels.getString("label.audit.excel.startString")) + 7, date.indexOf(Labels.getString("label.audit.excel.endString"))).trim();
			String endDate = date.substring(date.indexOf(Labels.getString("label.audit.excel.endString")) + 8, date.length()).trim();
			if (!startDate.equals(audit.getFromDate()) || !endDate.equals(audit.getToDate())) {
				throw new ValidationException(Messages.getString("audit.excel.not.interval.day"));
			}
		} else {
			throw new ValidationException(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.audit.excel.file")));
		}
	}
	
	public static boolean compareString(String s1, String s2) {
		s1 = s1.trim();
		s1 = s1.toLowerCase();
		s2 = s2.trim();
		s2 = s2.toLowerCase();
		if (s1.equals(s2)) {
			return true;
		} else {
			return false;
		}
	}
}
