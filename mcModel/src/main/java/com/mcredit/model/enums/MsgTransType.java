package com.mcredit.model.enums;

public enum MsgTransType {
	ISSCARD("ISSCARD"),
	W4SMSOTHER("W4SMSOTHER"),
	W4SMSTRANS("W4SMSTRANS"),
    W4SMSOTP("W4SMSOTP"),
    create_payment("create_payment"),
	PAYMENT_SMS("PAYMENT_SMS"),
	SMS("SMS"),
	NOTIFY_DEBT_SMS("NOTIFY_DEBT_SMS"),
	NOTIFY_DEBT_EMAIL("NOTIFY_DEBT_EMAIL"),
	UPDATE_CREDIT("UPDATE_CREDIT");
	
	private final String value;

	MsgTransType(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

}