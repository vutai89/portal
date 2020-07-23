package com.mcredit.model.enums;

public enum UplAction {
	UPL_DETAIL_APPROVE(1),
	UPL_DETAIL_REFUSE(2),
	UPL_DETAIL_DELETE(3),
	UPL_DETAIL_DELETE_EXITS(4);
	private final int value;

    private UplAction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
