package com.mcredit.model.dto.assign;

import java.util.List;

public class RoleServiceIdsDTO {
	private Long roleId;
	private List<Long> serviceIds;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public List<Long> getServiceIds() {
		return serviceIds;
	}

	public void setServiceIds(List<Long> serviceIds) {
		this.serviceIds = serviceIds;
	}
}
