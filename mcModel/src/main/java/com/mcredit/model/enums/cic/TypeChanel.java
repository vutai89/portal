package com.mcredit.model.enums.cic;

public enum TypeChanel {
	MCP("MCP"),
	PCB("PCB"),
	CIC("CIC");
	
	private final String value;
	TypeChanel(String value) {
		this.value = value;
	}
	
	public String value() {
		return this.value;
	}
	
	public static TypeChanel from(String text) {
        for (TypeChanel b : TypeChanel.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }

}
