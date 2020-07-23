package com.mcredit.model.object.mobile.enums;

public enum SubFolderEnum {
	ProofOfIncome("ProofOfIncome"),
	CustomerIdentification("CustomerIdentification"),
	ProofOfAddress("ProofOfAddress"),
	CreditDocuments("CreditDocuments"),
	OtherDocuments("OtherDocuments");
	
	private final String value;

	SubFolderEnum(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}
