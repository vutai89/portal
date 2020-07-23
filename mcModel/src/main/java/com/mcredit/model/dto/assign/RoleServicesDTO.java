package com.mcredit.model.dto.assign;

import java.util.ArrayList;
import java.util.List;

public class RoleServicesDTO {
	RoleDTO role;
	List<ServiceDTO> services;

	public RoleServicesDTO(RoleDTO role) {
		this.role = role;
	}
	
	public RoleDTO getRole() {
		return role;
	}

	public void setRole(RoleDTO role) {
		this.role = role;
	}

	public List<ServiceDTO> getServices() {
		return services;
	}

	public void setServices(List<ServiceDTO> services) {
		this.services = services != null ? services : new ArrayList<ServiceDTO>();
	}
}
