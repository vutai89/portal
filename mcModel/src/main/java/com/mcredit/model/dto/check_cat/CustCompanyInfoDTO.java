package com.mcredit.model.dto.check_cat;

public class CustCompanyInfoDTO {
	private Long id;
	private String name;
	private String taxNumber;
	private String address;
	private String phoneNumber;
	private String dateCheckCat;
	private String catType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTaxNumber() {
		return taxNumber;
	}

	public void setTaxNumber(String taxNumber) {
		this.taxNumber = taxNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getDateCheckCat() {
		return dateCheckCat;
	}

	public void setDateCheckCat(String dateCheckCat) {
		this.dateCheckCat = dateCheckCat;
	}

	public String getCatType() {
		return catType;
	}

	public void setCatType(String catType) {
		this.catType = catType;
	}

	

}
