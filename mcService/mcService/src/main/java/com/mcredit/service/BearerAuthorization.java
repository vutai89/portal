package com.mcredit.service;

import javax.ws.rs.core.HttpHeaders;

import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.manager.AuthorizationManager;
import com.mcredit.sharedbiz.validation.AuthorizationException;
import com.mcredit.util.StringUtils;

public class BearerAuthorization extends BaseAuthorization implements IAuthorization {

	public BearerAuthorization() {
	}
	
	public BearerAuthorization(HttpHeaders headers) {
		super(headers);
	}
	
	@Override
	public UserDTO authorize(String token) throws Exception {
		try (AuthorizationManager manager = new AuthorizationManager()){
			
			if (StringUtils.isNullOrEmpty(token))
				throw new AuthorizationException(exMes);
			
			return manager.checkToken(token);
		}
	} 
}
