package com.mcredit.model.enums;

public enum SMSType {
	
	SMSTYPE_NOTIFICATION("ThongBao");

	private final String value;

	SMSType(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

	public static SMSType fromString(String text) {
		for (SMSType b : SMSType.values()) {
			if (b.value.equalsIgnoreCase(text))
				return b;
		}
		return null;
	}
}
