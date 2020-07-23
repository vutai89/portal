package com.mcredit.model.object.cancelCaseBPM;

import java.io.Serializable;

public class AuthInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	protected String grant_type;
	protected String scope;
	protected String client_id;
	protected String client_secret;
	protected String username;
	protected String password;
	protected String refresh_token;

	public AuthInfo(String grant_type, String scope, String client_id, String client_secret, String username,String password, String refresh_token) {
		super();
		this.grant_type = grant_type;
		this.scope = scope;
		this.client_id = client_id;
		this.client_secret = client_secret;
		this.username = username;
		this.password = password;
		this.refresh_token = refresh_token;
	}

	public String getGrant_type() {
		return grant_type;
	}

	public void setGrant_type(String grant_type) {
		this.grant_type = grant_type;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getClient_secret() {
		return client_secret;
	}

	public void setClient_secret(String client_secret) {
		this.client_secret = client_secret;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

}
