package com.mcredit.sharedbiz.validation;

import java.util.List;

import com.mcredit.common.Messages;
import com.mcredit.model.dto.appbpm.AppReasonBPMDTO;
import com.mcredit.model.dto.appbpm.AppStatusBPMDTO;
import com.mcredit.util.StringUtils;

public class AppBPMValidation {
	// validate payload is null
	public void validateAppStatusBPM(AppStatusBPMDTO appStatus) throws ValidationException {
		if(appStatus == null || StringUtils.isNullOrEmpty(appStatus.getAppId())) {
			throw new ValidationException(Messages.getString("application.validate.payload.required"));
		}
	}
	
	// validate payload is null
	public void validateAppReasonBPM(List<AppReasonBPMDTO> appReasonLst) throws ValidationException {
		if(appReasonLst == null || appReasonLst.size() == 0) {
			throw new ValidationException(Messages.getString("application.validate.payload.required"));
		}
	}

}
