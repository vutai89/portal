package com.mcredit.model.enums.disaudit;

public enum FileFormat {
	
	MB_CHI("mcredit2"),
	MB_THU("mcredit1"),
	MB_SEPARATOR("#"),
	DIRECTORY_SEPARATOR("/"),
	HUY("_HUY"),
	VNPOST_CHI("PCH"),
	VNPOST_THU("ONLPAY")
	;
	
	private final Object value;
	
	FileFormat(Object value) {
		this.value = value;
	}
	
	public String value() {
		return (String) this.value;
	}
	
	public Long longValue() {
		return (Long)this.value;
	}
	
	public Integer intValue() {
		return (Integer)this.value;
	}
}
