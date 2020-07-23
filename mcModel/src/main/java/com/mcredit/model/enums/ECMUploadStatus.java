package com.mcredit.model.enums;

public enum ECMUploadStatus {

	NEW_UPLOAD("N"),
	PROCESSING("P"),
	GOT_FILE("F"),
	UPLOADED("U"),
	DELETED("D"),
	ERROR_UPLOAD("E");
	
	private final String value;

	ECMUploadStatus(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
    
}
