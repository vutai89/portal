package com.mcredit.business.black_list.validation;

import java.io.InputStream;
import java.util.List;

import com.mcredit.common.Messages;
import com.mcredit.model.dto.black_list.CustMonitorDTO;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;

public class BlackListValidation extends AbstractValidation {
	
	public void importBlackList(InputStream fileContent) throws ValidationException {
		
		if (null == fileContent)
			getMessageDes().add(Messages.getString("checkcic.param.required", "fileContent"));
		
		if(!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
	
	public void validateSave(List<CustMonitorDTO> list) throws ValidationException {
		if (list != null && list.size() == 0) {
			throw new ValidationException(Messages.getString("debt_home.assign"));
		}
	}
}
