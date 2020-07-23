package com.mcredit.model.enums.cic;

public enum CICRequestState {
	
	NEW("N"),
	ASSIGN("A"),
	RESULT("R"),
	EMAIL("M"),
	NOTIFY("P"),
	INACTIVE("I");
	
	private final String value;
	CICRequestState(String value) {
		this.value = value;
	}
	
	public String value() {
		return this.value;
	}
	
	public static CICRequestState from(String text) {
        for (CICRequestState b : CICRequestState.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }

}
