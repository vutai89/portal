package com.mcredit.model.enums;

public enum MailCheckErrEnums {
	
	CAVET("cavet"),
	CONTRACT("h\u1EE3p \u0111\u1ED3ng"),
	GET_DOC_ERR_UPDATE("gi\u1EA5y t\u1EDD update l\u1ED7i"),
	MAIL_TO("quanlyhoso_ttvh@mcredit.com.vn");
	
	private final String value;

	MailCheckErrEnums(String value) {
		this.value = value;
	}
	
	public static MailCheckErrEnums fromString(String text) {
	    for (MailCheckErrEnums mail : MailCheckErrEnums.values()) {
	      if (mail.value.equalsIgnoreCase(text))
	        return mail;
	    }
	    return null;
	}
	
	public String value() {
		return this.value;
	}

}
