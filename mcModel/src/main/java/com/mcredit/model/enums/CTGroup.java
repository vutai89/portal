package com.mcredit.model.enums;

public enum CTGroup {
	
	WARE_HOUSE("WH"),
	MISC("MISC"),
	MIS("MIS"),
	EMAIL("EMAIL"),
	INST("INST"),
	KIOSK("KIOSK"),
	HUB("HUB"),
	LOCA("LOCA"),
	PROD("PROD"),
	SALE("SALE"),
	STEP("STEP"), 
	CUST("CUST"),
	TRANS("TRANS"),
	CORP("CORP");
	
	private final String value;

	CTGroup(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
	
	public static CTGroup from(String text) {
        for (CTGroup b : CTGroup.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
