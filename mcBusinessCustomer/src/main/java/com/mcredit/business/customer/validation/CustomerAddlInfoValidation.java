package com.mcredit.business.customer.validation;

import com.mcredit.business.customer.dto.CustomerAddlInfoDTO;
import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.model.enums.CustomerValidationLength;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

public class CustomerAddlInfoValidation extends AbstractValidation {
	
	public void validateAddlInfo(CustomerAddlInfoDTO addlInfo) throws ValidationException {
		if( addlInfo!=null ) {
			
			if(addlInfo.getCustId() != null && addlInfo.getCustId() <= 0)
				getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.custId")));
			else if(String.valueOf(addlInfo.getCustId()).length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ID.value())
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.custId"), CustomerValidationLength.MAX_LEN_CUSTOMER_ID.value()));
			
			if(StringUtils.isNullOrEmpty(addlInfo.getRecordStatus()))
				getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.customer.accountLink.recordStatus")));
			else if(addlInfo.getRecordStatus().length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_TYPE.value())
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.accountLink.recordStatus"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_TYPE.value()));
			
			if(!StringUtils.validLength(addlInfo.getCreatedBy(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.accountLink.createdBy"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()));
			
			if(!StringUtils.validLength(addlInfo.getLastUpdatedBy(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.accountLink.lastUpdatedBy"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()));
			
			if(!StringUtils.validLength(addlInfo.getRefFullName1(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_NAME.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.addl.refFullName1"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_NAME.value()));
			
			if(!StringUtils.validLength(addlInfo.getRefFullName2(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_NAME.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.addl.refFullName2"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_NAME.value()));
			
			if(addlInfo.getIncomeSpouse()!=null && addlInfo.getIncomeSpouse() <= 0)
				getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.customer.addl.incomeSpouse")));
			else if(String.valueOf(addlInfo.getIncomeSpouse()).length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ID.value())
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.addl.incomeSpouse"), CustomerValidationLength.MAX_LEN_CUSTOMER_ID.value()));
			
			if(addlInfo.getLifetimeInMonth()!=null && addlInfo.getLifetimeInMonth() <= 0)
				getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.customer.addl.lifeTimeInMonth")));
			
			if(addlInfo.getLifetimeInYear()!=null && addlInfo.getLifetimeInYear() <= 0)
				getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.customer.addl.lifeTimeInYear")));
			
			if(!StringUtils.isNullOrEmpty(addlInfo.getRefPerson1Mobile()) && !StringUtils.checkMobilePhoneNumberNew(addlInfo.getRefPerson1Mobile()))
				getMessageDes().add(Messages.getString("validation.field.invalidFormat", Labels.getString("label.customer.addl.refPerson1Mobile")));
			
			if(!StringUtils.validLength(addlInfo.getRefPerson1Mobile(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_HOUSE_HOLD_REG_NUMBER.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.addl.refPerson1Mobile"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_HOUSE_HOLD_REG_NUMBER.value()));
			
			if(!StringUtils.isNullOrEmpty(addlInfo.getRefPerson2Mobile()) && !StringUtils.checkMobilePhoneNumberNew(addlInfo.getRefPerson2Mobile()))
				getMessageDes().add(Messages.getString("validation.field.invalidFormat", Labels.getString("label.customer.addl.refPerson2Mobile")));
			
			if(!StringUtils.validLength(addlInfo.getRefPerson2Mobile(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_HOUSE_HOLD_REG_NUMBER.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.addl.refPerson2Mobile"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_HOUSE_HOLD_REG_NUMBER.value()));
			
			if(!StringUtils.validLength(addlInfo.getSpouseCompanyName(), CustomerValidationLength.MAX_LEN_CUSTOMER_COMPANY_ADDRESS_STREET.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.addl.spouseCompanyName"), CustomerValidationLength.MAX_LEN_CUSTOMER_COMPANY_ADDRESS_STREET.value()));
			
			if(!StringUtils.validLength(addlInfo.getSpouseIdentityNumber(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_HOUSE_HOLD_REG_NUMBER.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.addl.spouseIdentityNumber"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_HOUSE_HOLD_REG_NUMBER.value()));
			
			if(!StringUtils.isNullOrEmpty(addlInfo.getSpouseMobile()) && !StringUtils.checkMobilePhoneNumberNew(addlInfo.getSpouseMobile()))
				getMessageDes().add(Messages.getString("validation.field.invalidFormat", Labels.getString("label.customer.addl.spouseMobile")));
			
			if(!StringUtils.validLength(addlInfo.getSpouseMobile(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_HOUSE_HOLD_REG_NUMBER.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.addl.spouseMobile"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_HOUSE_HOLD_REG_NUMBER.value()));
			
			if(!StringUtils.validLength(addlInfo.getSpouseName(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_NAME.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.addl.spouseName"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_NAME.value()));
			
			if(addlInfo.getTempSamePermAddr() != null && addlInfo.getTempSamePermAddr() <= 0)
				getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.customer.addl.tempSamePermAddr")));
			
			if(!StringUtils.validLength(addlInfo.getOldIdentityNumber(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_HOUSE_HOLD_REG_NUMBER.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.addl.oldIdentityNumber"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_HOUSE_HOLD_REG_NUMBER.value()));
			
			if(!StringUtils.validLength(addlInfo.getMilitaryId(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_HOUSE_HOLD_REG_NUMBER.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.addl.militaryId"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_HOUSE_HOLD_REG_NUMBER.value()));
			
			if(!StringUtils.validLength(addlInfo.getMilitaryIssuePlace(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_HOUSE_HOLD_REG_NUMBER.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.addl.militaryIssuePlace"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_HOUSE_HOLD_REG_NUMBER.value()));
			
			if(addlInfo.getLabourContractType()!=null && addlInfo.getLabourContractType() <= 0)
				getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.customer.addl.labourContractType")));
			
			if(addlInfo.getPayrollMethod()!=null && addlInfo.getPayrollMethod() <= 0)
				getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.customer.addl.payrollMethod")));
			
			if(addlInfo.getAccommodationType()!=null && addlInfo.getAccommodationType() <= 0)
				getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.customer.addl.accommodationType")));
			
			if(addlInfo.getIsBlackList()!=null && addlInfo.getIsBlackList() <= 0)
				getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.customer.addl.isBlackList")));
			
			if(addlInfo.getBlackListType()!=null && addlInfo.getBlackListType() <= 0)
				getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.customer.addl.blackListType")));
			
			if(addlInfo.getProfessional()!=null && addlInfo.getProfessional() <= 0)
				getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.customer.addl.professional")));
			
			if(!StringUtils.validLength(addlInfo.getDepartment(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_NAME.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.addl.militaryIssuePlace"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_NAME.value()));
			
			if(addlInfo.getPositionInComp()!=null && addlInfo.getPositionInComp() <= 0)
				getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.customer.addl.positionInComp")));
		}
		
		if(!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
}
