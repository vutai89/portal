package com.mcredit.model.enums;

public enum PaymentAmountTag {
	
	CRED_AMT("CRED_AMT"),
	CRED_AMT_MINUS_INSU("CRED_AMT_MINUS_INSU"),
	FEE_COLLECT_AMT("FEE_COLLECT_AMT"),
	TRANS_AMT("TRANS_AMT"),
	TRANS_AMT_MINUS_FEE("TRANS_AMT_MINUS_FEE");
	
	private String value;

	PaymentAmountTag(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
    
	public static PaymentAmountTag fromString(String text) {
		for (PaymentAmountTag b : PaymentAmountTag.values()) {
			if (b.value.equalsIgnoreCase(text))
				return b;
		}
		return null;
	}
}
