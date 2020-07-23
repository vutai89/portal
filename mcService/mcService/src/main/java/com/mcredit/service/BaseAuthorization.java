package com.mcredit.service;

import java.util.List;

import javax.ws.rs.core.HttpHeaders;

import com.mcredit.model.enums.AuthorizationToken;
import com.mcredit.model.object.AuthorizationTokenResult;
import com.mcredit.sharedbiz.validation.AuthorizationException;
import com.mcredit.util.StringUtils;

public class BaseAuthorization {
	protected HttpHeaders headers = null;
	private String AUTHORIZATION = "Authorization";
	private String AUTHORIZATION_CHEME_BEARER = "Bearer ";
	private String AUTHORIZATION_CHEME_BASIC = "Basic ";
	private String AUTHORIZATION_CHEME_OTT = "Ott ";
	protected String exMes = "Unauthorized.";
	
	public BaseAuthorization() {
	}
	
	public BaseAuthorization(HttpHeaders headers){
		this.headers = headers;
	}
		
	public AuthorizationTokenResult processHeader() throws Exception {
		
		AuthorizationTokenResult result = null;
		List<String> tempHeaders = headers.getRequestHeader(AUTHORIZATION);
		if (tempHeaders == null || tempHeaders.size() == 0)
			throw new AuthorizationException(exMes);

		String auth = tempHeaders.get(0);
		if (StringUtils.isNullOrEmpty(auth))
			throw new AuthorizationException(exMes);

		if (!auth.startsWith(AUTHORIZATION_CHEME_BEARER) && !auth.startsWith(AUTHORIZATION_CHEME_BASIC) && !auth.startsWith(AUTHORIZATION_CHEME_OTT))
			throw new AuthorizationException(exMes);
		
		String token = null;
		if(auth.startsWith(AUTHORIZATION_CHEME_BEARER)){
			token = auth.replaceFirst(AUTHORIZATION_CHEME_BEARER, StringUtils.Empty);
			result = new AuthorizationTokenResult(AuthorizationToken.BEARER,token);
		}
		
		if(auth.startsWith(AUTHORIZATION_CHEME_BASIC)){
			token = auth.replaceFirst(AUTHORIZATION_CHEME_BASIC, StringUtils.Empty);
			result = new AuthorizationTokenResult(AuthorizationToken.BASIC,token);
		}
		
		if(auth.startsWith(AUTHORIZATION_CHEME_OTT)){
			token = auth.replaceFirst(AUTHORIZATION_CHEME_OTT, StringUtils.Empty);
			result = new AuthorizationTokenResult(AuthorizationToken.OTT,token);
		}
		
		if(StringUtils.isNullOrEmpty(token))
			throw new AuthorizationException(exMes);
		
		return result;
	} 
}
