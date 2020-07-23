package com.mcredit.model.enums;


public enum ProductGroupEnum {
	INSTALLMENTLOAN("InstallmentLoan"),
	CONCENTRATINGDATAENTRY("ConcentratingDataEntry");
	
	private final String value;

	ProductGroupEnum(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

	public static ProductGroupEnum from(String text) {
        for (ProductGroupEnum b : ProductGroupEnum.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}