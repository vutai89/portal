package com.mcredit.model.enums;

public enum ValidateCreateCase {
	
	CREDIT_CARD("credit card"), // gia tri trong mobileProductType
	CASH_LOAN("cash loan"), // gia tri trong mobileProductType
	NEW_CASE(1), // khoi tao case moi
	RETURNED_CASE(2), // case tra ve
	MANDATORY(1), // bat buoc
	MIN_LENGTH_CITIZEND_ID(9), // do dai toi thieu CMND/CMQD
	MAX_LENGTH_CITIZEN_ID(12), // do dai toi da CMND/CMQD
	MAX_LENGTH_COMPANY_TAX_NUMBER(14), // do dai toi da ma so thue cong ty
	HAS_CHECK_CAT(1), // san pham co check cat
	MFS_CUST_NOT_NEED_BORROW(0L), // khach hang khong co nhu cau vay
	MFS_OTHER_REASON(1L), // li do khac
	PROD_INSTALLMENT_LOAN("2"), // Product in INSTALLMENT LOAN
	THIRD_PARTY("3") // id for third party in user type
	;
	
	private final Object value;
	
	ValidateCreateCase(Object value) {
		this.value = value;
	}

	public String value() {
		return this.value.toString();
	}
	
	public Long longValue() {
		return (Long)this.value;
	}
	
	public Integer intValue() {
		return (Integer)this.value;
	}
}
