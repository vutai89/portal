package com.mcredit.business.check_vehicle_price.validation;

import java.io.InputStream;

import com.mcredit.common.Messages;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;

public class CheckVehiclePriceValidation extends AbstractValidation {
	
	public void searchMotorPrice(Integer page, Integer rowPerPage) throws ValidationException {
		
		if (null == page || page < 1)
			getMessageDes().add("page không hợp lệ");
		
		if (null == rowPerPage || rowPerPage < 1)
			getMessageDes().add("rowPerPage không hợp lệ");
		
		if(!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
	
	public void uploadVehiclePrice(InputStream fileContent) throws ValidationException {
		
		if (null == fileContent)
			getMessageDes().add(Messages.getString("checkcic.param.required", "fileContent"));
		
		if(!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
}

