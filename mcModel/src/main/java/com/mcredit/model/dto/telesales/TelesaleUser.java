package com.mcredit.model.dto.telesales;

import java.io.Serializable;

public class TelesaleUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6074893972824358363L;
	
	private Long id;
	
	private String loginId;
	
	private String fullName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
}
