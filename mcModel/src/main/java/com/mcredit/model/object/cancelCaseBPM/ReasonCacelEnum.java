package com.mcredit.model.object.cancelCaseBPM;

public enum ReasonCacelEnum {
	AUTO_ABORT("H\u1EE7y t\u1EF1 \u0111\u1ED9ng");
	
	private final String value;

	ReasonCacelEnum(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
	
	public static ReasonCacelEnum from(String text) {
        for (ReasonCacelEnum b : ReasonCacelEnum.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
    
}
