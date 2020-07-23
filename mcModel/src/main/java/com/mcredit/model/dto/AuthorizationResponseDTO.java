package com.mcredit.model.dto;

import java.util.List;

public class AuthorizationResponseDTO {
	
	private String token;
	private Long userId;
	private Long bdsId;
	private String bdsCode;
	private String loginId;
	private String fullName;
	private boolean isSuperVisor;
	//private boolean isAsm;
	private boolean isTeamLead;
	private String empCode;
	private String userType;
	private String extNumber;
	private String deviceName;
	private List<MenuFunctionDTO> funcLst;
	private List<RoleDTO> roleCodeLst;
	private List<ServicePermissionDTO> serviceLst;

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getExtNumber() {
		return extNumber;
	}

	public void setExtNumber(String extNumber) {
		this.extNumber = extNumber;
	}

	public List<ServicePermissionDTO> getServiceLst() {
		return serviceLst;
	}

	public void setServiceLst(List<ServicePermissionDTO> serviceLst) {
		this.serviceLst = serviceLst;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public List<RoleDTO> getRoleCodeLst() {
		return roleCodeLst;
	}

	public void setRoleCodeLst(List<RoleDTO> roleCodeLst) {
		this.roleCodeLst = roleCodeLst;
	}

	public List<MenuFunctionDTO> getFuncLst() {
		return funcLst;
	}

	public void setFuncLst(List<MenuFunctionDTO> funcLst) {
		this.funcLst = funcLst;
	}

	public String getBdsCode() {
		return bdsCode;
	}

	public void setBdsCode(String bdsCode) {
		this.bdsCode = bdsCode;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public Long getBdsId() {
		return bdsId;
	}

	public void setBdsId(Long bdsId) {
		this.bdsId = bdsId;
	}

	public boolean isTeamLead() {
		return isTeamLead;
	}

	public void setTeamLead(boolean isTeamLead) {
		this.isTeamLead = isTeamLead;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public boolean isSuperVisor() {
		return isSuperVisor;
	}

	public void setSuperVisor(boolean isSuperVisor) {
		this.isSuperVisor = isSuperVisor;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public AuthorizationResponseDTO(String token) {
		setToken(token);
	}
	
	public AuthorizationResponseDTO(String token, Long userId, Long bdsId, String bdsCode, boolean isSuperVisor, boolean isTeamLead, String empCode, String extNumber, String deviceName,
			List<MenuFunctionDTO> funcLst, String loginId, String fullName, String userType, List<RoleDTO> roleCodeLst, List<ServicePermissionDTO> serviceLst) {
		setToken(token);
		setUserId(userId);
		setBdsId(bdsId);
		setBdsCode(bdsCode);
		setSuperVisor(isSuperVisor);
		//setAsm(isAsm);
		setTeamLead(isTeamLead);
		setEmpCode(empCode);
		setExtNumber(extNumber);
		setDeviceName(deviceName);
		setFuncLst(funcLst);
		setLoginId(loginId);
		setFullName(fullName);
		setUserType(userType);
		setRoleCodeLst(roleCodeLst);
		setServiceLst(serviceLst);
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
