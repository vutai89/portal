package com.mcredit.model.dto;

public class KioskDTO {
	private Integer id;
	private String kioskCode;
	private String kioskAddress;
	private String kioskProvince;

	public KioskDTO() {
	}

	public KioskDTO(Integer id, String kioskCode, String kioskAddress, String kioskProvince) {
		this.id = id;
		this.kioskCode = kioskCode;
		this.kioskAddress = kioskAddress;
		this.kioskProvince = kioskProvince;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKioskCode() {
		return kioskCode;
	}

	public void setKioskCode(String kioskCode) {
		this.kioskCode = kioskCode;
	}

	public String getKioskAddress() {
		return kioskAddress;
	}

	public void setKioskAddress(String kioskAddress) {
		this.kioskAddress = kioskAddress;
	}

	public String getKioskProvince() {
		return kioskProvince;
	}

	public void setKioskProvince(String kioskProvince) {
		this.kioskProvince = kioskProvince;
	}
}
