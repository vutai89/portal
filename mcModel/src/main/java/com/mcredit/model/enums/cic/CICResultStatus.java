package com.mcredit.model.enums.cic;

public enum CICResultStatus {
	NEW("NEW"),
	RENEW("RENEW"),
	SUCCESS("SUCCESS"),
	CHECKING("CHECKING");
	
	private final String value;
	CICResultStatus(String value) {
		this.value = value;
	}
	
	public String value() {
		return this.value;
	}
	
	public static CICResultStatus from(String text) {
        for (CICResultStatus b : CICResultStatus.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }

}
