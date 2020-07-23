package com.mcredit.sharedbiz.dto;

import java.io.Serializable;
import java.util.List;
import com.mcredit.data.user.entity.Users;
import com.mcredit.model.dto.MenuFunctionDTO;
import com.mcredit.model.dto.RoleDTO;
import com.mcredit.model.dto.ServicePermissionDTO;
import com.mcredit.util.StringUtils;

public class UserDTO extends Users implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1560387296903450952L;
	private List<MenuFunctionDTO> funcLst;
	private List<ServicePermissionDTO> serviceLst;
	private List<RoleDTO> roleCodeLst;
	
	public List<ServicePermissionDTO> getServiceLst() {
		return serviceLst;
	}

	public void setServiceLst(List<ServicePermissionDTO> serviceLst) {
		this.serviceLst = serviceLst;
	}

	public List<RoleDTO> getRoleCodeLst() {
		return roleCodeLst;
	}

	public void setRoleCodeLst(List<RoleDTO> roleCodeLst) {
		this.roleCodeLst = roleCodeLst;
	}

	public boolean isTsa() {
		return StringUtils.isNullOrEmpty(this.getSuperVisor()) && StringUtils.isNullOrEmpty(this.getTeamLead());
	}

	public boolean isSuperVisor() {
		return !StringUtils.isNullOrEmpty(this.getSuperVisor());
	}

	
	public boolean isTeamLead() {
		return !StringUtils.isNullOrEmpty(this.getTeamLead());
	}

	public List<MenuFunctionDTO> getFuncLst() {
		return funcLst;
	}

	public void setFuncLst(List<MenuFunctionDTO> funcLst) {
		this.funcLst = funcLst;
	}
}
