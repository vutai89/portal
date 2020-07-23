package com.mcredit.model.enums;

public enum LeadGenEnums {	
	REJECT("rejected"),
	PASSED("passed"),
	DEDUP("dedup"),
	DEDUP_1("dedup1"),
	DEDUP_2("dedup2"),
	CUST_IN_BLACK_LIST("blacklist"),
	DUPREF("dupRef"),
	ERROR("error"),
	BLACK_LIST("B"),
	WATCH_LIST("W"),
	APPROVING("approving"),		// trang thai lead data cho approved
	OK("OK"),
	LEAD_GEN("LEAD_GEN"),
	UPL_STATUS("L"),
	SCRIPT("SCRIPT"),
	
	SCORE_RANGE_LVL_1_NUM_START("550"), 
	SCORE_RANGE_LVL_1_NUM_END("600"),   
	SCORE_RANGE_LVL_2_NUM_START("601"), 
	SCORE_RANGE_LVL_2_NUM_END("645"),   
	SCORE_RANGE_LVL_3_NUM_START("646"), 
	SCORE_RANGE_LVL_3_NUM_END("850"),   
	
	SCORE_RANGE_LVL_1_PRO_CODE("C0000026"), //VT 60
	SCORE_RANGE_LVL_2_PRO_CODE("C0000025"), //VT 47
	SCORE_RANGE_LVL_3_PRO_CODE("C0000024"), //VT 37
	
	PREFIX_UPL_CODE("LeadGen-"),
	PARTNER_LEADGEN("LeadGen"),
	PARTNER_CICLG("CICLG"),
	PARTNER_TSNTB("TSNTB"),
	PARTNER_TRUST_CONNECT("TC"),
	PARTNER_AMO("AMO"),
	UPL_SRC("UPL_SRC"),
	SUCCESS("success"),
	
	PREFIX_CIC("CICLG-"),
	PREFIX_LEADGEN("LeadGen-"),
	PREFIX_TS("TSNTB-");

	
	private final String value;

	LeadGenEnums(String value) {
		this.value = value;
	}
	
	public static LeadGenEnums fromString(String text) {
	    for (LeadGenEnums b : LeadGenEnums.values()) {
	      if (b.value.equalsIgnoreCase(text))
	        return b;
	    }
	    return null;
	}
	
	public String value() {
		return this.value;
	}
}
