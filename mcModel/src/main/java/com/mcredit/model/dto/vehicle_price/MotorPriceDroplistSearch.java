package com.mcredit.model.dto.vehicle_price;

import java.util.List;

public class MotorPriceDroplistSearch {

	private List<String> listBrand;
	private List<String> listMotorType;
	private List<String> listMotorCode;
	
	public List<String> getListBrand() {
		return listBrand;
	}
	
	public void setListBrand(List<String> listBrand) {
		this.listBrand = listBrand;
	}
	
	public List<String> getListMotorType() {
		return listMotorType;
	}
	
	public void setListMotorType(List<String> listMotorType) {
		this.listMotorType = listMotorType;
	}
	
	public List<String> getListMotorCode() {
		return listMotorCode;
	}
	
	public void setListMotorCode(List<String> listMotorCode) {
		this.listMotorCode = listMotorCode;
	}
	
	
}
