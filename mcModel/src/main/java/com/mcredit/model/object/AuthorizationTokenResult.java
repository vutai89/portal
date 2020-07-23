package com.mcredit.model.object;

import com.mcredit.model.enums.AuthorizationToken;

public class AuthorizationTokenResult {

	private AuthorizationToken TokenEnum;
	private String token;

	public AuthorizationTokenResult(AuthorizationToken tokenEnum, String token) {
		super();
		TokenEnum = tokenEnum;
		this.token = token;
	}

	public AuthorizationToken getTokenEnum() {
		return TokenEnum;
	}

	public void setTokenEnum(AuthorizationToken tokenEnum) {
		TokenEnum = tokenEnum;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
