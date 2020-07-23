package com.mcredit.business.customer.validation;

import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.model.dto.card.CustomerAccountLinkDTO;
import com.mcredit.model.enums.CustomerValidationLength;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

public class CustomerAccountLinkValidation extends AbstractValidation {
	
	public void validate(CustomerAccountLinkDTO accountLink, String updateId) throws Exception {
			
		if(updateId!=null && (!StringUtils.isNumberic(updateId) || Long.parseLong(updateId) <= 0)) {
			getMessageDes().add(Messages.getString("validation.field.invalidFormat", Labels.getString("label.common.queryParam.isNumber")));
			throw new ValidationException(this.buildValidationMessage());
		}
		
		if( accountLink!=null ) {
			
			if(accountLink.getCustId()!=null && String.valueOf(accountLink.getCustId()).length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ID.value())
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.custId"), CustomerValidationLength.MAX_LEN_CUSTOMER_ID.value()));
			
			if(StringUtils.isNullOrEmpty(accountLink.getLinkType()))
				getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.customer.accountLink.linkType")));
			else if(accountLink.getLinkType().length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_TYPE.value())
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.accountLink.linkType"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_TYPE.value()));
			
			if(StringUtils.isNullOrEmpty(accountLink.getRecordStatus()))
				getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.customer.accountLink.recordStatus")));
			else if(accountLink.getRecordStatus().length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_TYPE.value())
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.accountLink.recordStatus"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_TYPE.value()));
			
			if(StringUtils.isNullOrEmpty(accountLink.getLinkSystem()))
				getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.customer.accountLink.linkSystem")));
			else if(accountLink.getLinkSystem().length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_SYSTEM.value())
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.accountLink.linkSystem"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_SYSTEM.value()));
			
			if(!StringUtils.validLength(accountLink.getCreatedBy(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.accountLink.createdBy"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()));
			
			if(!StringUtils.validLength(accountLink.getLastUpdatedBy(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.accountLink.lastUpdatedBy"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()));
			
			if(StringUtils.isNullOrEmpty(accountLink.getLinkValue()))
				getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.customer.accountLink.linkValue")));
			else if(accountLink.getLinkValue().length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value())
				getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.customer.accountLink.linkValue")));
			
			if(!StringUtils.validLength(accountLink.getLinkName(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_NAME.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.accountLink.linkName"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_NAME.value()));
			
			if(!StringUtils.validLength(accountLink.getLinkCurrency(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_SYSTEM.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.accountLink.linkCurrency"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_SYSTEM.value()));
			
			if(!StringUtils.validLength(accountLink.getLinkProduct(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_PRODUCT.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.accountLink.linkProduct"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_PRODUCT.value()));
			
			if(!StringUtils.validLength(accountLink.getAccountingCode(), CustomerValidationLength.MAX_LEN_CUSTOMER_CORE_CODE.value()))
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.accountLink.accountingCode"), CustomerValidationLength.MAX_LEN_CUSTOMER_CORE_CODE.value()));
		}
		
		if(!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
}
