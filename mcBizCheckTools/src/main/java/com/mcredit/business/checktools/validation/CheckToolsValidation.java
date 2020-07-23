package com.mcredit.business.checktools.validation;

import com.mcredit.common.Messages;
import com.mcredit.model.dto.checktools.ConditionInitContract;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

public class CheckToolsValidation extends AbstractValidation {
	
	public CheckToolsValidation() {
	}
	
	public void checkCitizenId(String productGroup, String citizenId) throws ValidationException {
		if (StringUtils.isNullOrEmpty(productGroup))
			getMessageDes().add(Messages.getString("checkcic.param.required", "productGroup"));
		
		if (StringUtils.isNullOrEmpty(citizenId))
			getMessageDes().add(Messages.getString("checkcic.param.required", "citizenId"));
		
		if(!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
	
	public void checkConditionInitContract(ConditionInitContract payload) throws ValidationException {
		if (null == payload)
			getMessageDes().add(Messages.getString("checkcic.payload.required"));
		else {
			if (StringUtils.isNullOrEmpty(payload.getProductGroup()))
				getMessageDes().add(Messages.getString("checkcic.param.required", "productGroup"));
			
			if (StringUtils.isNullOrEmpty(payload.getProductCode()))
				getMessageDes().add(Messages.getString("checkcic.param.required", "productCode"));
			
			if (StringUtils.isNullOrEmpty(payload.getCustomerName()))
				getMessageDes().add(Messages.getString("checkcic.param.required", "customerName"));
			
			if (StringUtils.isNullOrEmpty(payload.getCitizenId()))
				getMessageDes().add(Messages.getString("checkcic.param.required", "citizenId"));
			
			if (null == payload.getLoanAmount() || payload.getLoanAmount() <= 0)
				getMessageDes().add(Messages.getString("checkcic.param.required", "loanAmount"));
			
			if (null == payload.getLoanTenor() || payload.getLoanTenor() <= 0)
				getMessageDes().add(Messages.getString("checkcic.param.required", "loanTenor"));
			
			if (null == payload.getCustomerIncome() || payload.getCustomerIncome() <= 0)
				getMessageDes().add(Messages.getString("checkcic.param.required", "customerIncome"));
			
			if (StringUtils.isNullOrEmpty(payload.getDateOfBirth()))
				getMessageDes().add(Messages.getString("checkcic.param.required", "dateOfBirth"));
			
			if (StringUtils.isNullOrEmpty(payload.getGender()))
				getMessageDes().add(Messages.getString("checkcic.param.required", "gender"));
			
		}
		
		if(!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
	
}

