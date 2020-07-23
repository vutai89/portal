
package com.mcredit.model.enums;

public enum DataSizeEnum {

	BYTE_SIZE(1024);

	private final int value;

	DataSizeEnum(int value) {
		this.value = value;
	}

	public int intValue() {
		return this.value;
	}

}
