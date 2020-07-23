package com.mcredit.business.audit.validation;

import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.model.dto.audit.AuditCommandDTO;
import com.mcredit.model.enums.disaudit.ThirdParty;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.JSONConverter;
import com.mcredit.util.StringUtils;

public class DisAuditValidation extends AbstractValidation {
	
	public final static String dateFormat = "DDMMYYYY";

	public AuditCommandDTO validateAuditDC(String payload, String fileName) throws ValidationException {
		AuditCommandDTO auditCD = new AuditCommandDTO();
		if (!StringUtils.isNullOrEmpty(fileName)) {
			if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
				throw new ValidationException(Messages.getString("validation.field.invalidFormat",
						Labels.getString("label.cp.filename")));
			}			
		}
		try {
			auditCD = JSONConverter.toObject(payload, AuditCommandDTO.class);
			if (!auditCD.getThirdParty().equals(ThirdParty.VNPOST.value())) {
				auditCD.setTime(null);
			}
		} catch (Exception ex) {
//			throw new ValidationException();
		}
		
		return auditCD;
		
	}

	public void validateGetResult(String thirdParty, String fromDate, String toDate, String reportType, String result) throws ValidationException {
		if (StringUtils.isNullOrEmpty(thirdParty)) {
			throw new ValidationException(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.audit.get.thirdParty")));
		}
		if (StringUtils.isNullOrEmpty(fromDate)) {
			throw new ValidationException(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.audit.get.fromDate")));
		}
		if (StringUtils.isNullOrEmpty(toDate)){
			throw new ValidationException(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.audit.get.toDate")));
		}
		if (StringUtils.isNullOrEmpty(reportType)) {
			throw new ValidationException(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.audit.get.reportType")));
		}
		if (StringUtils.isNullOrEmpty(result)) {
			throw new ValidationException(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.audit.get.result")));
		}
		
	}
}

