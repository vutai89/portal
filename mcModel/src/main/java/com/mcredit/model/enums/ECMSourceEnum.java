package com.mcredit.model.enums;

public enum ECMSourceEnum {

	COLLECTION("C"),
	CASH_FROM_BPM("S"),
	INSTALLMENT_FROM_BPM("I"),
	DATA_CENTRALIZE_FROM_BPM("D"),
	FAS("F");
	
	private final String value;

	ECMSourceEnum(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
	
	public static ECMSourceEnum from(String text) {
        for (ECMSourceEnum b : ECMSourceEnum.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
