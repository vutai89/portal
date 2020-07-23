package com.mcredit.model.enums;

public enum PostingConfigurationDebitOwner {

	CUSTOMER("C"), PARTNER("P"), UNUSED("U"), INTERNAL("I");

	private String value;

	PostingConfigurationDebitOwner(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

	public static PostingConfigurationDebitOwner fromString(String text) {
		for (PostingConfigurationDebitOwner b : PostingConfigurationDebitOwner.values()) {
			if (b.value.equalsIgnoreCase(text))
				return b;
		}
		return null;
	}
}
