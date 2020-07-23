package com.mcredit.model.enums;

public enum TypeDoc {
	TYPE_DOCUMENT("CODE_TABLE"), TYPE_DOCUMENT_BORROW("CODE_TABLE_BRROW");
	private String value;

	TypeDoc(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

	public static CacheTag fromString(String text) {
		for (CacheTag b : CacheTag.values()) {
			if (b.value().equalsIgnoreCase(text))
				return b;
		}
		return null;
	}

	public static boolean contains(String value) {

		for (CacheTag c : CacheTag.values()) {
			if (c.value().equals(value)) {
				return true;
			}
		}

		return false;
	}
}
