package com.mcredit.business.customer.validation;

import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;

public class CheckDuplicateValidation extends AbstractValidation {

	public void validate(String identityNumber) throws ValidationException {
		
		/*if( !StringUtils.isNumberic(identityNumber) || ( StringUtils.isNumberic(identityNumber) && !Arrays.asList(new String[]{"9", "12"}).contains(identityNumber.length() + "") ) )
			throw new ValidationException(Labels.getString("label.customer.queryInfo.identityNumberInvalid"));
		
		if ( identityNumber.length() > 15 )
			throw new ValidationException(Labels.getString("label.customer.queryInfo.militaryIdInvalid"));*/
		
		if ( identityNumber.length() > 15 )
			throw new ValidationException(Messages.getString("validation.field.invalidFormat", Labels.getString("label.customer.queryInfo.identityInvalid")));
	}
}
