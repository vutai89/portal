package com.mcredit.business.mobile.validation;

import java.text.ParseException;

import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.model.dto.mobile.ReportDTO;
import com.mcredit.model.object.warehouse.Document;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.DateUtil;

public class ReportValidation extends AbstractValidation{
	
	public void validateReport(String dateExport) throws ValidationException, ParseException {
		
		if (dateExport == null || dateExport.length() == 0) {
			getMessageDes().add(Messages.getString("validation.field.empty", Labels.getString("label.mobile.notEmpty")));
		}else {
			if(!DateUtil.validateFormat(dateExport,"yyyy-MM-dd")) {
				getMessageDes().add(Messages.getString("validation.field.empty", Labels.getString("label.mobile.notFormatted")));
			}
		}
		if (!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
}
