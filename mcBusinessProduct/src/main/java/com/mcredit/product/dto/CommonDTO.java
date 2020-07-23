package com.mcredit.product.dto;

import java.util.List;

public class CommonDTO {
	public CommonDTO() {
		// TODO Auto-generated constructor stub
	}

	// Chi bao gom id, name, code
	private String sValue1;
	private String sValue2;
	private Integer iValue1;
	private Integer iValue2;
	private String type;
	private Object data;
	private List<Object> lstData;
	public String getsValue1() {
		return sValue1;
	}
	public void setsValue1(String sValue1) {
		this.sValue1 = sValue1;
	}
	public String getsValue2() {
		return sValue2;
	}
	public void setsValue2(String sValue2) {
		this.sValue2 = sValue2;
	}
	public Integer getiValue1() {
		return iValue1;
	}
	public void setiValue1(Integer iValue1) {
		this.iValue1 = iValue1;
	}
	public Integer getiValue2() {
		return iValue2;
	}
	public void setiValue2(Integer iValue2) {
		this.iValue2 = iValue2;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public List<Object> getLstData() {
		return lstData;
	}
	public void setLstData(List<Object> lstData) {
		this.lstData = lstData;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
