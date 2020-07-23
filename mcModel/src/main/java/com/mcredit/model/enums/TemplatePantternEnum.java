package com.mcredit.model.enums;

public enum TemplatePantternEnum {
	DUE_DATE("DUE_DATE"),
	CARD_ID("CARD_ID"),
	DUE_BALANCE("DUE_BALANCE"),
	REMAIN_MIN_AMOUNT("REMAIN_MIN_AMOUNT");
	
	private final String value;

	TemplatePantternEnum(String value) {
		this.value = value;
	}

  public static TemplatePantternEnum fromString(String text) {
	    for (TemplatePantternEnum b : TemplatePantternEnum.values()) {
	      if (b.value.equalsIgnoreCase(text)) {
	        return b;
	      }
	    }
	    return null;
	  }

	public String value() {
		return this.value;
	}

}
