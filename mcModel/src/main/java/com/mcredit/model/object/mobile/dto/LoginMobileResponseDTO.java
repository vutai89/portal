package com.mcredit.model.object.mobile.dto;

import java.util.List;

import com.mcredit.model.dto.MenuFunctionDTO;
import com.mcredit.model.dto.RoleDTO;

public class LoginMobileResponseDTO {
	private String userId;
	private String userName;
	private String token;
	private List<RoleDTO> roleCodeLst;
	List<MenuFunctionDTO> funcLst;
	
	
	public List<MenuFunctionDTO> getFuncLst() {
		return funcLst;
	}
	public void setFuncLst(List<MenuFunctionDTO> funcLst) {
		this.funcLst = funcLst;
	}
	public LoginMobileResponseDTO(String userId, String userName, String token,
			List<RoleDTO> roleCodeLst, List<MenuFunctionDTO> funcLst) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.token = token;
		this.roleCodeLst = roleCodeLst;
		this.funcLst = funcLst;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public List<RoleDTO> getRoleCodeLst() {
		return roleCodeLst;
	}
	public void setRoleCodeLst(List<RoleDTO> roleCodeLst) {
		this.roleCodeLst = roleCodeLst;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
