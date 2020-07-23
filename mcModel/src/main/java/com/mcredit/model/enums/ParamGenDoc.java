package com.mcredit.model.enums;

public enum ParamGenDoc {
	
	GENDOCS("GENDOCS"),
	V_DATA_ENTRY_APPNUMBER("V_DATA_ENTRY_APPNUMBER"),
	V_DATA_ENTRY_APPNUMBER_DE("V_DATA_ENTRY_APPNUMBER_DE"),
	GENDOC_VERSION("GENDOC_VERSION"),
        V_DATA_ENTRY_APPID("V_DATA_ENTRY_APPID"),
	V_DATA_ENTRY_APPID_DE("V_DATA_ENTRY_APPID_DE");
	
	private final String value;

	ParamGenDoc(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
	
	public static ParamGenDoc from(String text) {
        for (ParamGenDoc b : ParamGenDoc.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
    
}
