package com.mcredit.business.telesales.validation;

import java.util.Arrays;

import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.model.enums.CustomerValidationLength;
import com.mcredit.model.leadgen.LeadDTO;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

public class AmoValidation extends AbstractValidation {
	
	public void validateSendLead(LeadDTO input) throws ValidationException {
		
		if ( input == null ) {
			getMessageDes().add(Messages.getString("validation.field.invalidFormat", Labels.getString("label.telesale.checkDuplicate.contractSearch.input")));
			throw new ValidationException(BusinessConstant.JSON_NAME_ERROR, this.buildValidationMessage(), "");
		}
		
		if ( StringUtils.isNullOrEmpty(input.getRefId()) )
			getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.leadgen.refId")));
		else if( input.getRefId().length() > 20 )
			getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.leadgen.refId"), CustomerValidationLength.MAX_LEN_CUSTOMER_CORE_CODE.value()));
		
		if ( StringUtils.isNullOrEmpty(input.getPhoneNumber()) )
			getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.leadgen.phoneNumber")));
		else if ( !StringUtils.checkMobilePhoneNumberNew(input.getPhoneNumber()) )
			getMessageDes().add(Messages.getString("validation.field.invalidFormat", Labels.getString("label.leadgen.phoneNumber")));
		
		if ( !StringUtils.isNullOrEmpty(input.getNationalId()) ) {
			if ( !StringUtils.isNumberic((input.getNationalId())) )
				getMessageDes().add(Messages.getString("validation.field.invalidFormat", Labels.getString("label.leadgen.nationalId")));
			else if ( !Arrays.asList(new String[]{"9", "12"}).contains(input.getNationalId().length() + "") )
				getMessageDes().add(Messages.getString("label.leadgen.nationalId.inRange", Labels.getString("label.leadgen.nationalId")));
		}
		
		if ( !StringUtils.isNullOrEmpty(input.getDob()) && input.getDob().length() > 15 )
			getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.personal.birthDate"), CustomerValidationLength.MAX_LEN_PROVINCE.value()));
		
		if ( !StringUtils.isNullOrEmpty(input.getProvince()) && StringUtils.getUTFSize(input.getProvince()) > 50 )
			getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.leadgen.province"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_HOUSE_HOLD_REG_NUMBER.value()));
		
		if ( !StringUtils.isNullOrEmpty(input.getFullName()) && StringUtils.getUTFSize(input.getFullName()) > 100 )
			getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.leadgen.fullName"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_NAME.value()));
		
		if ( !isValid() )
			throw new ValidationException(BusinessConstant.JSON_NAME_ERROR, this.buildValidationMessage(), "");
	}
}
