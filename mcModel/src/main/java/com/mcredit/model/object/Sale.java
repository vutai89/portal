package com.mcredit.model.object;

import java.io.Serializable;


public class Sale implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3073730472606989931L;

	private String loginId;
	
	private String saleCode;
	
	private String saleName;
	
	private String position;

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getSaleCode() {
		return saleCode;
	}

	public void setSaleCode(String saleCode) {
		this.saleCode = saleCode;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getSaleName() {
		return saleName;
	}

	public void setSaleName(String saleName) {
		this.saleName = saleName;
	}
}
