package com.mcredit.model.enums;

public enum OwnerTag {

	OWNERTAG_ACCOUNT("ACCOUNT"), OWNERTAG_CARDID("CARDID"), OWNERTAG_LOANLD("LOANLD");

	private String value;

	OwnerTag(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

}
