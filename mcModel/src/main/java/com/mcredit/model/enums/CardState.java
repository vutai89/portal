package com.mcredit.model.enums;

public enum CardState {
	
	CARD_OK("00"),
	CARD_DO_NOT_HONOR("01"),
	CARD_NO_RENEWAL_BADEBT("37"),
	PIN_TRIES_EXECEEDED("02"),
	CARD_EXPIRED_ZCODE_76("76"),
	PICKUP_04("04"),
	PickUp_L_41("41");
	
	/*PIN_BLOCKED("PIN Blocked"),
	NO_RENEWAL("Card No Renewal"),
	NO_RENEWAL_BADEBT("Card No Renewal - Badebt"),
	NO_RENEWAL_VISA("Card No Renewal - VISA"),
	CLOSED("Card Closed"),
	EXPIRED("Card Expired"),
	HONOUR_WITH_ID("Honour with identification"),
	LOCKED_BY_CARD_HOLDER("Locked by main cardholder"),
	PICK_UP("PickUp L 41"),
	PIN_INVALID("PIN invalid"),
	Z_CODE_08("zCODE 08"),
	Z_CODE_38("zCODE 38"),
	Z_CODE_58("zCODE 58"),
	Z_CODE_61("zCODE 61"),
	Z_CODE_65("zCODE 65"),
	Z_CODE_66("zCODE 66"),
	Z_CODE_67("zCODE 67");*/
	
	private final String value;

	CardState(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
    
	public static CardState fromString(String text) {
		for (CardState b : CardState.values()) {
			if (b.value.equalsIgnoreCase(text))
				return b;
		}
		return null;
	}
}
