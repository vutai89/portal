package com.mcredit.model.enums;

public enum PaymentValidationLength {

	
	MAX_LEN_PAYMENT_PARTNERREFID(20), MAX_LEN_PAYMENT_POSTINGGROUP(3);
	
	private final int value;

	PaymentValidationLength(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}
}
