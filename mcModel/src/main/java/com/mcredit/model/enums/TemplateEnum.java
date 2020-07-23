package com.mcredit.model.enums;

public enum TemplateEnum {
	WH_CHECK_ERROR_OP_2("WH_CHECK_ERROR_OP_2"),
	MFS_CHANGE_STATUS("MFS_CHANGE_STATUS"),
	SCHEDULE_PRODUCT_CR("SCHEDULE_PRODUCT_CR"),
	SCHEDULE_PRODUCT_AC("SCHEDULE_PRODUCT_AC"),
	MAIL_AUTO_ABORT("MAIL_AUTO_ABORT"),
	MAIL_CIC_NOTI("MAIL_CIC_NOTI")
	;

	
	private final String value;

	TemplateEnum(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
	
	public static TemplateEnum from(String text) {
        for (TemplateEnum b : TemplateEnum.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}


