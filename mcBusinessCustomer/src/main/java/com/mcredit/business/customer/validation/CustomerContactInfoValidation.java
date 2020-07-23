package com.mcredit.business.customer.validation;

import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.model.dto.card.CustomerAddressInfoDTO;
import com.mcredit.model.dto.card.CustomerCommunicationInfoDTO;
import com.mcredit.model.dto.card.CustomerContactInfoDTO;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

public class CustomerContactInfoValidation extends AbstractValidation {
	
	public void validateContactInfo(CustomerContactInfoDTO contactInfo) throws ValidationException {
		if( contactInfo!=null ) {
			
			if( contactInfo.getCommunicationInfo()==null || contactInfo.getCommunicationInfo().size()==0  )
				getMessageDes().add(Messages.getString("validation.field.invalidFormat", Labels.getString("label.customer.contactInfo.communicationInfoRequired")));
			else {
				for( CustomerCommunicationInfoDTO communication : contactInfo.getCommunicationInfo() ) {
					if( StringUtils.isNullOrEmpty(communication.getCategory()) )
						getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.customer.contactInfo.communicationInfo.category")));
					if( StringUtils.isNullOrEmpty(communication.getType()) )
						getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.customer.contactInfo.communicationInfo.type")));
					//if( StringUtils.isNullOrEmpty(communication.getContactValue()) )
						//getMessageDescCollection().add(Messages.getString("validation.field.madatory", Labels.getString("label.customer.contactInfo.communicationInfo.contactValue")));
				}
			}
			
			if( contactInfo.getAddressInfo()==null || contactInfo.getAddressInfo().size()==0  )
				getMessageDes().add(Messages.getString("validation.field.invalidFormat", Labels.getString("label.customer.contactInfo.addressInfoRequired")));
			else {
				for( CustomerAddressInfoDTO address : contactInfo.getAddressInfo() ) {
					if( StringUtils.isNullOrEmpty(address.getType()) )
						getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.customer.contactInfo.addressInfo.type")));
					//if( StringUtils.isNullOrEmpty(address.getAddress()) )
						//getMessageDescCollection().add(Messages.getString("validation.field.madatory", Labels.getString("label.customer.contactInfo.addressInfo.address")));
				}
			}
		}
		
		if(!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
}
