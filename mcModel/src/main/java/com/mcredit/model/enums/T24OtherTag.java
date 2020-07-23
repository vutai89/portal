package com.mcredit.model.enums;

public enum T24OtherTag {
	OTHER_CustomerID("CustomerID"),
	OTHER_CARDID("CardID"),
	OTHER_RECORDID("RecordID"),
	WAY4_ISSUE_STATUS_SUCCESS("00"),
	WAY4_ISSUE_STATUS_FAIL("01");
	

	private final String value;

	T24OtherTag(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}
