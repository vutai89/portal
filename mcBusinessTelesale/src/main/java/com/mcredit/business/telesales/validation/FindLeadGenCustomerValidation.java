package com.mcredit.business.telesales.validation;

import java.util.Arrays;

import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

public class FindLeadGenCustomerValidation extends AbstractValidation {

	public void validate(String identityNumber, String mobileNumber) throws ValidationException {
		
		if( !StringUtils.isNullOrEmpty(identityNumber) ) {
			if( !StringUtils.isNumberic(identityNumber) || (StringUtils.isNumberic(identityNumber) && !Arrays.asList(new String[]{"9", "12"}).contains(identityNumber.length() + "")) )
				getMessageDes().add(Messages.getString("validation.field.invalidFormat", Labels.getString("label.customer.queryInfo.identityNumberInvalid")));
		}
		
		if ( StringUtils.isNullOrEmpty(mobileNumber) )
			getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.leadgen.phoneNumber")));
		else if ( !StringUtils.checkMobilePhoneNumberNew(mobileNumber) )
			getMessageDes().add(Messages.getString("validation.field.invalidFormat", Labels.getString("label.leadgen.phoneNumber")));
		
		if(!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
}
