package com.mcredit.model.dto.assign;

import java.util.List;

public class FunctionRolePermission {
	private Long roleId;
	
	private List<Long> functionId;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public List<Long> getFunctionId() {
		return functionId;
	}

	public void setFunctionId(List<Long> functionId) {
		this.functionId = functionId;
	}
}
