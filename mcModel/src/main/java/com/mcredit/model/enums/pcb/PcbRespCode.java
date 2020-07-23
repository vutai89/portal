package com.mcredit.model.enums.pcb;

public enum PcbRespCode {
	PCBSUCCESSCODE("200"),
	PCBFAILCODE("999");
	
	private final String value;
	
	PcbRespCode(String value) {
		this.value = value;
	}
	
	public String value() {
		return this.value;
	}
	
	public static PcbRespCode from(String text) {
        for (PcbRespCode b : PcbRespCode.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
