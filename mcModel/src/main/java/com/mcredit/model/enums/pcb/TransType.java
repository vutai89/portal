package com.mcredit.model.enums.pcb;

public enum TransType {
	PCB_QUERY("PCB_QUERY"),
	MC_QUERY("MC_QUERY");
	
	private final String value;
	TransType(String value) {
		this.value = value;
	}
	
	public String value() {
		return this.value;
	}
	
	public static TransType from(String text) {
        for (TransType b : TransType.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
