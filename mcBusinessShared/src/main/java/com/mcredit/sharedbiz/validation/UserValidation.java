package com.mcredit.sharedbiz.validation;

import com.mcredit.model.dto.AuthorizationRequestDTO;
import com.mcredit.util.StringUtils;

public class UserValidation {
	public static void validateAuthorization(AuthorizationRequestDTO request) throws ValidationException{
		if(request == null)
			throw new ValidationException("Username and Password input is required.");
		
		if(StringUtils.isNullOrEmpty(request.getUsername()))
			throw new ValidationException("Username is required.");
		
		if(StringUtils.isNullOrEmpty(request.getPassword()))
			throw new ValidationException("Password is required.");
	}

}
