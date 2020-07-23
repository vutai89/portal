package com.mcredit.business.pcb.validation;

import com.mcredit.common.Messages;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

public class PCBValidation extends AbstractValidation {
	public void validateCheckPcb(Object[] payload, String appId, String idCard, String birthday) throws ValidationException {
		if (StringUtils.isNullOrEmpty(idCard) || payload ==  null || StringUtils.isNullOrEmpty(birthday)) {
			throw new ValidationException(Messages.getString("pcb.appId.payload.required"));
		}
	}
}
