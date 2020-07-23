package com.mcredit.model.dto.telesales;

public class ProspectCallingPermissionDTO {
	private String permission;

	public ProspectCallingPermissionDTO(String permission){
		this.permission = permission;
	}
	
	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

}
