package com.mcredit.model.enums.ocr;

public enum ContractEnum {
	GIONG("M"),
	KHAC("D"),
	TIEN_MAT("0"),
	TIEN_MAT_BAO_HIEM("1"),
	TRA_GOP("2"),
	TRA_GOP_BAO_HIEM("3")
	;
	
	private final String value;
	
	ContractEnum(String value) {
		this.value = value;
	}
	
	public String value() {
		return this.value;
	}

}
