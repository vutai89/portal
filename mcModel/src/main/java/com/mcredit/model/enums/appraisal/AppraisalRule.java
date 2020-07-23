package com.mcredit.model.enums.appraisal;

public enum AppraisalRule {
	
	AGE("AGE"),
	INCOME_1("INCOME_1"),		// thu nhap noi suy
	INCOME_2("INCOME_2"),		// thu nhap tham dinh
	LOAN_AMOUNT_AFTER_INSURANCE("LOAN_AMOUNT_AFTER_INSURANCE"),
	LOAN_TENOR("LOAN_TENOR"),
	PREPAY_RATE("PREPAY_RATE"),
	APPRAISAL("APPRAISAL"),
	NO_OF_RELATION_ORGANIZE("NO_OF_RELATION_ORGANIZE"),
	NO_OF_RELATION_COMPANY("NO_OF_RELATION_COMPANY"),
	OUTSTANDING_CIC("OUTSTANDING_CIC"),
	PTI("PTI"),
	DTI("DTI"),
	LOAN_AMOUNT("LOAN_AMOUNT"),
	GOODS_("GOODS_");
	
	private final String value;
	
	AppraisalRule(String value) {
		this.value = value;
	}
	
	public String value() {
		return this.value;
	}
	
}
