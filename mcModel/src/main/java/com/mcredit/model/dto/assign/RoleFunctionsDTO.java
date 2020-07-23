package com.mcredit.model.dto.assign;

import java.util.List;

public class RoleFunctionsDTO {
	private Long id;
	private String createdBy;
	private String lastUpdatedBy;
	private String roleCode;
	private String roleType;
	private String roleName;
	private String status;
	private List<FunctionDTO> functions;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getRoleType() {
		return roleType;
	}
	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
	public String getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<FunctionDTO> getFunctions() {
		return functions;
	}
	public void setFunctions(List<FunctionDTO> functions) {
		this.functions = functions;
	}
}
