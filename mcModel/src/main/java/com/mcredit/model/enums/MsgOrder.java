package com.mcredit.model.enums;

public enum MsgOrder {
	ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5),
	NINETY_NINE(99);

	private final int value;

	MsgOrder(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

}
