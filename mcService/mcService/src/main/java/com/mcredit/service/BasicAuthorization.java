package com.mcredit.service;

import javax.ws.rs.core.HttpHeaders;

import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.manager.AuthorizationManager;
import com.mcredit.sharedbiz.validation.AuthorizationException;
import com.mcredit.util.StringUtils;
import com.mcredit.util.security.Base64Converter;

public class BasicAuthorization extends BaseAuthorization implements IAuthorization {
	
	public BasicAuthorization() {
	}
	
	public BasicAuthorization(HttpHeaders headers) {
		super(headers);
	}

	private UserDTO _userDTO = null;
	

	@Override
	public UserDTO authorize(String token) throws Exception {
		try (AuthorizationManager manager = new AuthorizationManager()){
			
			String tempToken = Base64Converter.decode(token);
			if(StringUtils.isNullOrEmpty(tempToken))
				throw new AuthorizationException(exMes);
			
			if(tempToken.indexOf(":") == -1)
				throw new AuthorizationException(exMes);
			
			String[] credential = tempToken.split(":");
			if(credential == null || credential.length != 2)
				throw new AuthorizationException(exMes);
			
			String username = credential[0];
			String password = credential[1];
			
			if(StringUtils.isNullOrEmpty(username) || StringUtils.isNullOrEmpty(password))
				throw new AuthorizationException(exMes);
			
			_userDTO = manager.authorized(username, password);
			
			return _userDTO;
		}
	} 
}
