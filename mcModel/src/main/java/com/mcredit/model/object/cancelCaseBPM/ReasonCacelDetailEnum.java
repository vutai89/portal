package com.mcredit.model.object.cancelCaseBPM;

public enum ReasonCacelDetailEnum {
	APPROVAL_RESULTS_EXPIRE("K\u1EBFt qu\u1EA3 ph\u00EA duy\u1EC7t h\u1EBFt hi\u1EC7u l\u1EF1c"),
	EXPIRY_OF_ADDITIONAL_PAPER_TIME("H\u1EBFt th\u1EDDi h\u1EA1n b\u1ED5 sung gi\u1EA5y t\u1EDD"),
	THE_VALIDITY_PERIOD_OF_THE_RECEIVING_CODE_EXPIRES("H\u1EBFt th\u1EDDi h\u1EA1n hi\u1EC7u l\u1EF1c c\u1EE7a m\u00E3 nh\u1EADn ti\u1EC1n"),
	TIME_LIMIT_FOR_PROCESSING_DOCUMENTS("H\u1EBFt th\u1EDDi h\u1EA1n x\u1EED l\u00FD h\u1ED3 s\u01A1"),
	EXPIRY_DATE_OF_DATA_ENTRY("H\u1EBFt th\u1EDDi h\u1EA1n nh\u1EADp li\u1EC7u");
	
	private final String value;

	ReasonCacelDetailEnum(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

	public static ReasonCacelDetailEnum from(String text) {
		for (ReasonCacelDetailEnum b : ReasonCacelDetailEnum.values()) {
			if (b.value.equalsIgnoreCase(text)) {
				return b;
			}
		}
		return null;
	}

}
