package com.mcredit.model.enums;

public enum DuplicateContractDescription {

	CASE_REJECT_IN_3_MONTHS("CASE_REJECT_IN_3_MONTHS"),		// ho so bi tu choi trong vong 3 thang
	PAYMENT_TENOR_NOT_ENOUGH("PAYMENT_TENOR_NOT_ENOUGH"),	// ho so tra duoi 3 ky no hoac co no nhom 2 tro len trong 12 thang
	CASE_PROCESSING("CASE_PROCESSING"),						// ho so dang o trang thai DE, DC, CA, AP, OP (return, open, pending)
	PAYMENT_TENOR_EQUAL_3("PAYMENT_TENOR_EQUAL_3");			// ho so tra dung 3 ky
	
	private final String value;
	
	DuplicateContractDescription(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}
