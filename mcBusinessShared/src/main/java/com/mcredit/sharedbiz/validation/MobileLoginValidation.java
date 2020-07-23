package com.mcredit.sharedbiz.validation;

import com.mcredit.model.object.mobile.dto.LoginMobileRequestDTO;
import com.mcredit.util.StringUtils;

public class MobileLoginValidation {
	
	public static void validateAuthorization(LoginMobileRequestDTO request) throws ValidationException{
		if(request == null) {
			throw new ValidationException("Username and Password input is required.");
		}
		
		if(StringUtils.isNullOrEmpty(request.getUsername())) {
			throw new ValidationException("Username is required.");
		}
		if(StringUtils.isNullOrEmpty(request.getPassword())) {
			throw new ValidationException("Password is required.");
		}
		
		if(StringUtils.isNullOrEmpty(request.getImei())) {
			throw new ValidationException("Imei is required.");
		}
		
		if(StringUtils.isNullOrEmpty(request.getOsType())) {
			throw new ValidationException("OsType is required.");
		}
	}

}
