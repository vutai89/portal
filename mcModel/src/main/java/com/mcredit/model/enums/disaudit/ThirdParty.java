package com.mcredit.model.enums.disaudit;

public enum ThirdParty {
	ALL("ALL"),
	VIETTEL("VIETTEL"),
	VNPOST("VNPOST"),
	MOMO("MOMO"),
	PAYOO("PAYOO"),
	VNPTEPAY("VNPTEPAY"), 
	VNPOST17("VNPOST17"),
	MB("MB")
	;
	
	private final String value;
	
	ThirdParty(String value) {
		this.value = value;
	}
	
	public String value() {
		return this.value;
	}
}
