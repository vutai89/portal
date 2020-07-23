package com.mcredit.model.dto.assign;

public class MenuDTO {
	private Long id;
	private String recordStatus;
	private String module;
	private String menuCode;
	private String menuTitle;
	private String menuMessageKey;
	private Long parentMenu;
	private String status;
	private String menuType;
	private Long functionId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public String getMenuTitle() {
		return menuTitle;
	}

	public void setMenuTitle(String menuTitle) {
		this.menuTitle = menuTitle;
	}

	public String getMenuMessageKey() {
		return menuMessageKey;
	}

	public void setMenuMessageKey(String menuMessageKey) {
		this.menuMessageKey = menuMessageKey;
	}

	public Long getParentMenu() {
		return parentMenu;
	}

	public void setParentMenu(Long parentMenu) {
		this.parentMenu = parentMenu;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMenuType() {
		return menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	public Long getFunctionId() {
		return functionId;
	}

	public void setFunctionId(Long functionId) {
		this.functionId = functionId;
	}

	public MenuDTO() {
		super();
	}

}
