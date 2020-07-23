package com.mcredit.business.payment.validation;

import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.model.dto.card.PaymentDTO;
import com.mcredit.model.enums.PaymentValidationLength;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

public class PaymentValidation extends AbstractValidation {

	public void validatePayment(PaymentDTO item) throws ValidationException {
		
		if (item != null) {

			if(StringUtils.isNullOrEmpty(item.getCardId()))
				getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.payment.cardId")));
			
			if(StringUtils.isNullOrEmpty(item.getPartnerCode()))
				getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.payment.partnerCode")));
			
			if(StringUtils.isNullOrEmpty(item.getPartnerRefId()))
				getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.payment.partnerRefId")));
			
			if(StringUtils.isNullOrEmpty(item.getPartnerRefId()) &&  item.getPartnerRefId().length() > PaymentValidationLength.MAX_LEN_PAYMENT_PARTNERREFID.value())
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.payment.partnerRefId"), PaymentValidationLength.MAX_LEN_PAYMENT_PARTNERREFID.value()));
			
			if(StringUtils.isNullOrEmpty(item.getPostingGroup()))
				getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.payment.postingGroup")));
			
			if(item.getAmount()==null)
				getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.payment.amount")));

			if (!isValid())
				throw new ValidationException(this.buildValidationMessage());
		}
	}
}
