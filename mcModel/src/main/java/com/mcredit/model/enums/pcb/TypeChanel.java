package com.mcredit.model.enums.pcb;

public enum TypeChanel {
	MCP("MCP"),
	PCB("PCB");
	
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
