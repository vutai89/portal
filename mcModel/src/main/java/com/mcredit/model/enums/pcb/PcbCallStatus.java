package com.mcredit.model.enums.pcb;

public enum PcbCallStatus {
	
	PCBSUCCESSCHECK("S"),
	PCBFAILCHECK("E");
	
	private final String value;
	
	PcbCallStatus(String value) {
		this.value = value;
	}
	
	public String value() {
		return this.value;
	}
	
	public static PcbCallStatus from(String text) {
        for (PcbCallStatus b : PcbCallStatus.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
