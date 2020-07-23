package com.mcredit.business.customer.validation;

import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.model.dto.card.CustomerCompanyInfoDTO;
import com.mcredit.model.enums.CustomerValidationLength;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

public class CustomerCompanyInfoValidation extends AbstractValidation {
	
	public void validateCompanyInfo(CustomerCompanyInfoDTO companyInfo) throws ValidationException {
		if( companyInfo!=null ) {
			
			/*if(companyInfo.getCustId() != null && companyInfo.getCustId() <= 0)
				getMessageDescCollection().add(Messages.getString("validation.field.integer", Labels.getString("label.custId")));
			else if(String.valueOf(companyInfo.getCustId()).length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ID.value())
				getMessageDescCollection().add(Messages.getString("validation.field.length", Labels.getString("label.custId"), CustomerValidationLength.MAX_LEN_CUSTOMER_ID.value()));*/
			
			if(StringUtils.isNullOrEmpty(companyInfo.getRecordStatus()))
				getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.customer.accountLink.recordStatus")));
			else if(companyInfo.getRecordStatus().length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_TYPE.value())
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.accountLink.recordStatus"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_TYPE.value()));
			
			if(!StringUtils.validLength(companyInfo.getCreatedBy(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.accountLink.createdBy"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()));
			
			if(!StringUtils.validLength(companyInfo.getLastUpdatedBy(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.accountLink.lastUpdatedBy"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()));
			
			if(!StringUtils.validLength(companyInfo.getCompAddrStreet(), CustomerValidationLength.MAX_LEN_CUSTOMER_COMPANY_ADDRESS_STREET.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.company.addressStreet"), CustomerValidationLength.MAX_LEN_CUSTOMER_COMPANY_ADDRESS_STREET.value()));
			
			if(companyInfo.getCompAddrProvince()!=null && companyInfo.getCompAddrProvince() <= 0)
				getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.customer.company.addressProvince")));
			
			if(companyInfo.getCompAddrDistrict()!=null && companyInfo.getCompAddrDistrict() <= 0)
				getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.customer.company.addressDistrict")));
			
			if(companyInfo.getCompAddrWard()!=null && companyInfo.getCompAddrWard() <= 0)
				getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.customer.company.addressWard")));
			
			/*if(StringUtils.isNullOrEmpty(companyInfo.getCompName()))
				getMessageDescCollection().add(Messages.getString("validation.field.madatory", Labels.getString("label.customer.company.name")));
			else */if(!StringUtils.isNullOrEmpty(companyInfo.getCompName()) && companyInfo.getCompName().length() > CustomerValidationLength.MAX_LEN_CUSTOMER_COMPANY_ADDRESS_STREET.value())
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.company.name"), CustomerValidationLength.MAX_LEN_CUSTOMER_COMPANY_ADDRESS_STREET.value()));
			
			if(!StringUtils.validLength(companyInfo.getCompTaxNumber(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_HOUSE_HOLD_REG_NUMBER.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.company.officePhoneNumber"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_HOUSE_HOLD_REG_NUMBER.value()));
			
			if(!StringUtils.validLength(companyInfo.getOfficePhoneNumber(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_HOUSE_HOLD_REG_NUMBER.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.company.officePhoneNumber"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_HOUSE_HOLD_REG_NUMBER.value()));
		}
		
		if(!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
}
