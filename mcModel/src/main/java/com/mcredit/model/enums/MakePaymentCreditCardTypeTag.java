package com.mcredit.model.enums;

public enum MakePaymentCreditCardTypeTag {
	VAL_MAKE_PAYMENT_TRANSFERTYPE_INHOUSE("INHOUSE"),
	VAL_MAKE_PAYMENT_TRANSACTIONTYPE_ACPH("ACPH"),
	VAL_MAKE_PAYMENT_TRANSACTIONTYPE_ACCP("ACCP"),
	VAL_MAKE_PAYMENT_ACCOUNTTYPE_ACCOUNT("ACCOUNT"),
	VAL_MAKE_PAYMENT_ACCOUNTTYPE_ACCOUNT_NAME("T\u00E0i kho\u1EA3n kh\u00E1ch h\u00E0ng th\u1EBB t\u00EDn d\u1EE5ng"),
	VAL_MAKE_PAYMENT_ACCOUNTTYPE_ACCOUNT_NAME_CREDIT("4521 GIU HO CHO TT PAYOO"),
	VAL_MAKE_PAYMENT_CREDITBANK_CODE("VN0010001"),
	VAL_MAKE_PAYMENT_CREDITBANK_NAME(""),
	VAL_MAKE_PAYMENT_AMOUNT_CURRENCY("VND");	
	
	private String value;

	MakePaymentCreditCardTypeTag(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}