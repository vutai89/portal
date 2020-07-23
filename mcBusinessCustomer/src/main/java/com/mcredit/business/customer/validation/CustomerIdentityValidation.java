package com.mcredit.business.customer.validation;

import java.util.List;

import com.mcredit.business.customer.dto.CustomerIdentityDTO;
import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.model.enums.CustomerValidationLength;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

public class CustomerIdentityValidation extends AbstractValidation {
	
	public void validateIdentity(List<CustomerIdentityDTO> LstIdentity) throws ValidationException {
		
		if( LstIdentity!=null && LstIdentity.size()>0 ) {
			
			for( CustomerIdentityDTO identity : LstIdentity ) {
				
				if(identity.getCustId() != null && identity.getCustId() <= 0)
					getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.custId")));
				else if(String.valueOf(identity.getCustId()).length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ID.value())
					getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.custId"), CustomerValidationLength.MAX_LEN_CUSTOMER_ID.value()));
				
				if(StringUtils.isNullOrEmpty(identity.getRecordStatus()))
					getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.customer.accountLink.recordStatus")));
				else if(identity.getRecordStatus().length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_TYPE.value())
					getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.accountLink.recordStatus"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_TYPE.value()));
				
				if(!StringUtils.validLength(identity.getCreatedBy(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()))
					getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.accountLink.createdBy"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()));
				
				if(!StringUtils.validLength(identity.getLastUpdatedBy(), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()))
					getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.accountLink.lastUpdatedBy"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()));
				
				if(StringUtils.isNullOrEmpty(identity.getIdentityTypeIdValue()))
					getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.customer.identity.identityTypeId")));
				
				/*if(StringUtils.isNullOrEmpty(identity.getIdentityIssuePlaceValue()))
					getMessageDescCollection().add(Messages.getString("validation.field.madatory", Labels.getString("label.customer.identity.identityIssuePlace")));
				
				if( identity.getIdentityIssueDate() == null )
					getMessageDescCollection().add(Messages.getString("validation.field.madatory", Labels.getString("label.customer.identity.identityIssueDate")));*/
				
				if(StringUtils.isNullOrEmpty(identity.getIdentityNumber()))
					getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.customer.identity.identityNumber")));
				else if(identity.getIdentityNumber().length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value())
					getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.customer.identity.identityNumber"), CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()));
				
				if(identity.getIdentityIssuePlace() != null && identity.getIdentityIssuePlace() <= 0)
					getMessageDes().add(Messages.getString("validation.field.integer", Labels.getString("label.customer.identity.identityIssuePlace")));
			}
		}
		
		if(!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
}
