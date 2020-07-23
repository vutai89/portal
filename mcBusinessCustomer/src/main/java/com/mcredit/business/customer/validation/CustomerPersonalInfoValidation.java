package com.mcredit.business.customer.validation;

import com.mcredit.business.customer.dto.CustomerPersonalInfoDTO;
import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.model.enums.CustomerValidationLength;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

public class CustomerPersonalInfoValidation extends AbstractValidation {
	
	public void validatePersonalInfo(CustomerPersonalInfoDTO personalInfo, String updateId) throws ValidationException {
		
		if( personalInfo!=null ) {
			
			if(updateId!=null && (!StringUtils.isNumberic(updateId) || Long.parseLong(updateId) <= 0)) {
				getMessageDes().add(Messages.getString("validation.field.invalidFormat", Labels.getString("label.common.queryParam.isNumber")));
				throw new ValidationException(this.buildValidationMessage());
			}
			
			if(StringUtils.isNullOrEmpty(personalInfo.getRecordStatus()))
				getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.customer.accountLink.recordStatus")));
			else if(personalInfo.getRecordStatus().length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_TYPE.value())
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.accountLink.recordStatus"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_TYPE.value()));
			
			//if(!StringUtils.validLength(personalInfo.getCreatedBy(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE))
				//getMessageDescCollection().add(Messages.getString("validation.field.length", Labels.getString("label.customer.accountLink.createdBy"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE));
			
			if(!StringUtils.validLength(personalInfo.getLastUpdatedBy(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.accountLink.lastUpdatedBy"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()));
			
			if(!StringUtils.validLength(personalInfo.getCoreCustCode(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.personal.coreCode"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()));
			
			if(StringUtils.isNullOrEmpty(personalInfo.getCustName()))
				getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.customer.personal.custName")));
			else if(personalInfo.getCustName().length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_NAME.value())
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.personal.custName"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_NAME.value()));
			
			if(StringUtils.isNullOrEmpty(personalInfo.getShortCustName()))
				getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.customer.personal.shortCustName")));
			else if(personalInfo.getShortCustName().length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_NAME.value())
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.personal.shortCustName"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_NAME.value()));
			
			if(!StringUtils.validLength(personalInfo.getOtherCustName(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_NAME.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.personal.ortherCustName"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_NAME.value()));
			
			if(personalInfo.getIdentityId() != null && String.valueOf(personalInfo.getIdentityId()).length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ID.value())
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.personal.identityId"), CustomerValidationLength.MAX_LEN_CUSTOMER_ID.value()));
			
			if(StringUtils.isNullOrEmpty(personalInfo.getGenderValue()))
				getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.customer.personal.gender")));
			
			if(personalInfo.getBirthDate()==null)
				getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.customer.personal.birthDate")));
			
			if(!StringUtils.validLength(personalInfo.getHouseholdRegNumber(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_HOUSE_HOLD_REG_NUMBER.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.personal.houseHoldRegNumber"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_HOUSE_HOLD_REG_NUMBER.value()));
			
			if(!StringUtils.validLength(personalInfo.getHouseholdRegIssuePlace(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_NAME.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.personal.houseHoldRegIssuePlace"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_NAME.value()));
			
			/*if(!StringUtils.isNullOrEmpty(personalInfo.getHomePhone()) && !StringUtils.checkMobilePhoneNumber(personalInfo.getHomePhone()))
				getMessageDescCollection().add(Messages.getString("validation.field.invalidFormat", Labels.getString("label.customer.personal.homePhone")));*/
			
			if(!StringUtils.validLength(personalInfo.getHomePhone(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_HOUSE_HOLD_REG_NUMBER.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.personal.homePhone"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_HOUSE_HOLD_REG_NUMBER.value()));
			
			if(!StringUtils.validLength(personalInfo.getIsProspect(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_TYPE.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.personal.isProspect"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_TYPE.value()));
		}
		
		if(!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
}
