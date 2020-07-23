package com.mcredit.business.ap.convert;

import java.util.List;

import com.mcredit.data.user.entity.Users;
import com.mcredit.model.dto.assign.FunctionDTO;
import com.mcredit.model.dto.assign.MenuDTO;
import com.mcredit.model.dto.assign.RoleDTO;
import com.mcredit.model.dto.assign.RoleFunctionsDTO;
import com.mcredit.model.dto.assign.RoleMenusDTO;
import com.mcredit.model.dto.assign.UserRolesDTO;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.validation.ValidationException;

public class Converter {
	public static UserRolesDTO convert(Users users, List<RoleDTO> roles) throws Exception {
		UserRolesDTO userRoleDTO = new UserRolesDTO();

		userRoleDTO.setId(users.getId());
		userRoleDTO.setLoginId(users.getLoginId());
		userRoleDTO.setFullName(users.getUsrFullName());
		if(!(users.getUsrType()==null)) {
			userRoleDTO.setUserType(CacheManager.CodeTable().getbyID(Integer.valueOf(users.getUsrType())).getDescription1());
		}
		userRoleDTO.setCreatedBy(users.getCreatedBy());
		userRoleDTO.setCreatedDate(users.getCreatedDate());
		userRoleDTO.setRoles(roles);

		return userRoleDTO;
	}

	public static RoleMenusDTO convertMenu(RoleDTO role, List<MenuDTO> menus)
			throws NumberFormatException, ValidationException {
		RoleMenusDTO roleMenuDTO = new RoleMenusDTO();
		roleMenuDTO.setId(role.getId());
		roleMenuDTO.setCreatedBy(role.getCreatedBy());
		roleMenuDTO.setLastUpdatedBy(role.getLastUpdatedBy());
		roleMenuDTO.setRoleCode(role.getRoleCode());
		roleMenuDTO.setRoleName(role.getRoleName());
		roleMenuDTO.setRoleType(role.getRoleType());
		roleMenuDTO.setStatus(role.getStatus());
		roleMenuDTO.setMenus(menus);

		return roleMenuDTO;
	}
	
	public static RoleFunctionsDTO convertFunction(RoleDTO role, List<FunctionDTO> functions)
			throws NumberFormatException, ValidationException {
		RoleFunctionsDTO roleFunctionDTO = new RoleFunctionsDTO();
		roleFunctionDTO.setId(role.getId());
		roleFunctionDTO.setCreatedBy(role.getCreatedBy());
		roleFunctionDTO.setLastUpdatedBy(role.getLastUpdatedBy());
		roleFunctionDTO.setRoleCode(role.getRoleCode());
		roleFunctionDTO.setRoleName(role.getRoleName());
		roleFunctionDTO.setRoleType(role.getRoleType());
		roleFunctionDTO.setStatus(role.getStatus());
		roleFunctionDTO.setFunctions(functions);

		return roleFunctionDTO;
	}
}
