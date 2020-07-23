package com.mcredit.model.enums;

public enum CustomerValidationLength {
	
	MAX_LEN_CUSTOMER_CORE_CODE(20),
	MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE(30),
	MAX_LEN_CUSTOMER_ACCOUNT_LINK_NAME(100),
	MAX_LEN_CUSTOMER_ACCOUNT_HOUSE_HOLD_REG_NUMBER(50),
	MAX_LEN_CUSTOMER_COMPANY_ADDRESS_STREET(255),
	MAX_LEN_CUSTOMER_ID(13),
	MAX_LEN_PROVINCE(15),
	MAX_LEN_ADDRESS(50),
	MAX_LEN_CUSTOMER_ACCOUNT_LINK_TYPE(1),
	MAX_LEN_CUSTOMER_ACCOUNT_LINK_SYSTEM(3),
	MAX_LEN_CUSTOMER_ACCOUNT_LINK_PRODUCT(10);
	
	private final int value;

	CustomerValidationLength(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}
    
}
