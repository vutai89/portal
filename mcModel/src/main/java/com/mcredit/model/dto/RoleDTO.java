package com.mcredit.model.dto;

import java.io.Serializable;

public class RoleDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 65742261949219529L;

	private String roleCode;

	public RoleDTO(String roleCode) {
		this.roleCode = roleCode;
	}
	
	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
}
