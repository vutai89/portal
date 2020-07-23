package com.mcredit.model.enums.pcb;

public enum PCBContractPhase {
	
	Requested("RQ"),
	Renounced("RN"),
	Refused("RF"),
	Living("LV"),
	Terminated("TM"),
	TerminatedInadvance("TA");
	
	private final String value;
	
	PCBContractPhase(String value) {
		this.value = value;
	}
	
	public String value() {
		return this.value;
	}
	
	public static PCBContractPhase from(String text) {
        for (PCBContractPhase b : PCBContractPhase.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
