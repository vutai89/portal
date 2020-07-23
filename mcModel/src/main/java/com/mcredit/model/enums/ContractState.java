package com.mcredit.model.enums;

public enum ContractState {
	
	CANCEL("CANCEL"),
	CLOSE("CLOSE"),
	EARLY_PAYMENT("EARLY.PAYMENT"),
	BACK_DATE("BACKDATE"),
	//BAD_DEBT("BADDEBT"),
	OPEN("OPEN");
	
	private final String value;

	ContractState(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
    
	public static ContractState fromString(String text) {
		for (ContractState b : ContractState.values()) {
			if (b.value.equalsIgnoreCase(text))
				return b;
		}
		return null;
	}
}
