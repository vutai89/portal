package com.mcredit.model.dto.warehouse;

public class WhQRCodeDTO {

	private Long Id;
	private String qrCode;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

}
