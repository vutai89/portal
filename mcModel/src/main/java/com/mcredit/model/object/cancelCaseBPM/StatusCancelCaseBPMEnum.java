package com.mcredit.model.object.cancelCaseBPM;

public enum StatusCancelCaseBPMEnum {

	POS_PENDING("POS_PENDING"),
	POS_OPEN("POS_OPEN"),
	POS_RETURN_PENDING("POS_RETURN_PENDING"),
	POS_RETURN_OPEN("POS_RETURN_OPEN"),
	DONE("DONE"),
	SALE_RETURN_PENDING("SALE_RETURN_PENDING"),
	SALE_RETURN_OPEN("SALE_RETURN_OPEN"),
	DE_RETURN_PENDING("DE_RETURN_PENDING"),
	DE_RETURN_OPEN("DE_RETURN_OPEN"),
	DE_ONLINE_RETURN_PENDING("DE_ONLINE_RETURN_PENDING"),
	DE_ONLINE_RETURN_OPEN("DE_ONLINE_RETURN_OPEN"),
	SALE_OPEN("SALE_OPEN"),
	DE_OPEN("DE_OPEN");
	
	private final String value;

	StatusCancelCaseBPMEnum(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
	
	public static StatusCancelCaseBPMEnum from(String text) {
        for (StatusCancelCaseBPMEnum b : StatusCancelCaseBPMEnum.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
    
}
