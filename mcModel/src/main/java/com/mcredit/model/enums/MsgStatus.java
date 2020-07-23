package com.mcredit.model.enums;

public enum MsgStatus {
    
    NEW("N"),
	PROCESSING("P"),
	SUCCESS("S"),
	DONE("DONE"),
	ERROR("E"),
	TIME_OUT("T"),
	IGNORE("I");
	
	private final String value;

	MsgStatus(String value) {
		this.value = value;
	}

  public static MsgStatus fromString(String text) {
	    for (MsgStatus b : MsgStatus.values()) {
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
