package com.mcredit.model.enums;

public enum MsgChannel {
	
	WAY4_APPLICATION("W4"),
	T24_APPLICATION("T24"),
	MC_PORTAL_APPLICATION("MCP"),
	BPM("BPM");
	
	private final String value;

	MsgChannel(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
    
	public static MsgChannel fromString(String text) {
		for (MsgChannel b : MsgChannel.values()) {
			if (b.value.equalsIgnoreCase(text))
				return b;
		}
		return null;
	}
}
