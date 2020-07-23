package com.mcredit.model.object.cancelCaseBPM;

public enum NewStatusEnum {

	POS_AUTO_ABORT("POS_AUTO_ABORT"),
	DONE_AUTO_ABORT("DONE_AUTO_ABORT"),
	SALE_AUTO_ABORT("SALE_AUTO_ABORT"),
	DE_AUTO_ABORT("DE_AUTO_ABORT");
	
	private final String value;

	NewStatusEnum(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
	
	public static NewStatusEnum from(String text) {
        for (NewStatusEnum b : NewStatusEnum.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
    
}