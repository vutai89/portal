package com.mcredit.model.enums;

public enum WHStep {
	
	INPUTCASE("INPUTCASE"),
	FOLLOWCASE("FOLLOWCASE"),
	ALLOCATION("ALLOCATION"),
	LOAD_BATCH("LOAD_BATCH"),
	APPROVE("APPROVE"),
	OPERATOR_TWO("OPERATOR_TWO"),
	LODGE_CONTRACT("LODGE_CONTRACT"),
	LODGE_CAVET("LODGE_CAVET"),
	BORROW("BORROW"),
	RETURN_CAVET("RETURN_CAVET"),
	BORROW_CONTRACT("BORROW_CONTRACT"),
	BORROW_CAVET("BORROW_CAVET"),
	THANK_LETTER("THANK_LETTER");
	
	private final String value;

	WHStep(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
	
	public static WHStep from(String text) {
        for (WHStep b : WHStep.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
    
}
