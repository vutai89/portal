package com.mcredit.model.dto.card;

import java.io.Serializable;

public class CustomerAddressInfoDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String type;
	private Integer addrType;
	
	private String addrProvinceValue;
	private Integer province;
	
	private String addrDistrictValue;
	private Integer district;
	
	private String addrWardValue;
	private Integer ward;
	
	private String address;
	
	private String recordStatus;
	private String createdBy;
	private String lastUpdatedBy;

	public CustomerAddressInfoDTO() {
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getAddrType() {
		return addrType;
	}

	public void setAddrType(Integer addrType) {
		this.addrType = addrType;
	}

	public String getAddrProvinceValue() {
		return addrProvinceValue;
	}

	public void setAddrProvinceValue(String addrProvinceValue) {
		this.addrProvinceValue = addrProvinceValue;
	}

	public Integer getProvince() {
		return province;
	}

	public void setProvince(Integer province) {
		this.province = province;
	}

	public String getAddrDistrictValue() {
		return addrDistrictValue;
	}

	public void setAddrDistrictValue(String addrDistrictValue) {
		this.addrDistrictValue = addrDistrictValue;
	}

	public Integer getDistrict() {
		return district;
	}

	public void setDistrict(Integer district) {
		this.district = district;
	}

	public String getAddrWardValue() {
		return addrWardValue;
	}

	public void setAddrWardValue(String addrWardValue) {
		this.addrWardValue = addrWardValue;
	}

	public Integer getWard() {
		return ward;
	}

	public void setWard(Integer ward) {
		this.ward = ward;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}