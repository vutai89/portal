package com.mcredit.model.enums;

public enum CTCat {
	
	GENDER("GENDER"),
	
	HOUSE_HOLD_REG_TYPE_ID("HHREG_TYPE"),
	
	MARITAL_STATUS("MARITAL"),
	
	RELATION_SPOUSE("RELAT_SPOU"),
	
	RELATION_REF_PERSON("RELAT_REF"),
	
	SPOUSE_POSITION("POSITION"),
	
	EDUCATION("EDUCATION"),
	
	TEMP_SAME_PERM_ADDR("TMP_RES"),
	
	LABOUR_CONTRACT_TYPE("LAB_CONT"),
	
	PAYROOL_METHOD("PAYROLL_M"),
	
	ACCOMMODATION_TYPE("ACCOM_TYPE"),
	
	IS_BLACK_LIST("BLACK_LIST"),
	
	BLACK_LIST_TYPE("BLACK_TYPE"),
	
	PROFESSION("PROFESSION"),
	
	POSITION_IN_COMP("POSITION"),
	
	S37_DATA("CIC_S37"),
	
	LIFE_INSU_COMPANY_ID("LINS_COMP"),
	
	INSU_TERM("INSU_TERM"),
	
	DISTRICT("DISTRICT"),
	
	CONTAC_TYP("CONTAC_TYP"),
	
	CONTAC_CAT("CONTAC_CAT"),
	
	ADDR_TYPE("ADDR_TYPE"),
	
	PROVINCE("PROVINCE"),
	
	WARD("WARD"),
	
	CURRENT_ADDR_SPOUSE("SP_ADDR"),
	
	IDENTITY_ISSUE_PLACE("IDPLACE"),
	
	CA_TENOR("CA_TENOR"),
	
	TRAN_OFF("TRAN_OFF"),
	
	APPLY_OBJ("APPLY_OBJ"),
	
	INSU_COMP("INSU_COMP"),
	
	INSU_STFID("INSU_STFID"),
	
	INSU_TYPE("INSU_TYPE"),
	
	DIS_CHANN("DIS_CHANN"),
	
	DIS_METHOD("DIS_METHOD"),
	
	DUPLICATE("DUPLICATE"),
	
	CA_PURPOSE("CA_PURPOSE"),
	
	DIS_STATUS("DIS_STATUS"),
	
	CARD_RADD("CARD_RADD"),
	
	FRE_COLANN("FRE_COLANN"),
	
	FRE_COLINS("FRE_COLINS"),
	
	INDUSTRY("INDUSTRY"),
	
	IDENTITY_TYPE_ID("IDTYP"), 
	
	BOOLEAN("BOOLEAN"),
	
	SEND_STATUS("SEND_STATUS"),
	EMAIL_TYPE("EMAIL_TYPE"),
	WH_MATERIAL("WH_MATERIAL"),	
	WH_DOC_TYPE("WH_DOC_TYPE"),	
	WH_CHAN_TYPE("WH_CHAN_TYPE"),	
	WH_CAVET_TYPE("WH_CAVET_TYPE"),	
	WH_C_LODGE_CV("WH_C_LODGE_CV"),
	WH_LODGE("WH_LOGDE"),
	WH_STATUS_DOC("WH_STATUS_DOC"),
	WH_APR_STATUS("WH_APR_STATUS"),
	WH_WAIT("WH_WAIT"),
	WH_WAIT_RETURN("WH_WAIT_RETURN"),
	WH_RE_STATUS("WH_RE_STATUS"),
	WH_ASS_STATUS("WH_ASS_STATUS"),
	WH_DOC_TYPE_CHANGE("WH_DOC_TYPE_CHANGE"),
	WH_BRW_TYPE("WH_BRW_TYPE"),
    WH_SAVE_TRANSFER("WH_SAVE_TRANSFER"),
    WH_HISTORY_EXPORT("WH_HISTORY_EXPORT"),
        
    //Other module
  	UPL_SRC("UPL_SRC"),
  	UPL_TYPE("UPL_TYPE"),
  	STATUS_APP_XSELL("STATUS_APP_XSELL"), 
  	COMM("COMM"),
  	BRAND("BRAND"),
  	MODEL("MODEL"),

  	
  	//Mobile4Sale
  	MFS_ABORT_REASON("MFS_ABORT_REASON"),

    WH_HIS_EXPECTED_DATE("WH_HIS_EXPECTED_DATE"),
    EM_POS_TS("EM_POS_TS"),
    STEP_STAT("STEP_STAT"), 
    WH_ASS_TYPE("WH_ASS_TYPE"),
    WH_LOGDE("WH_LOGDE"), WH_FLOW("WH_FLOW"), PRD_GROUP("PRD_GROUP"), POS_SIP("POS_SIP"),
    WH_CHANGE_STATUS("WH_CHANGE_STATUS"), BUS_SEGM("BUS_SEGM"), WH_PRG_STATUS("WH_PRG_STATUS"), WH_UPDATE_ERR("WH_UPDATE_ERR"), IDTYP("IDTYP"), IDPLACE("IDPLACE"),
    TRANS_OP_CODE("TRANS_OP_CODE"),
    TRANS_STATUS("TRANS_STATUS"),

    
    MULTINATIONAL_COMP("MULTINATIONAL_COMP"),
  	TOP_COMP("TOP_COMP"),
  	TAX_CHAPTER_GROUP("TAX_CHAPTER_GROUP"),
  	CAT_TYPE("CAT_TYPE"),
  	CORP_CIC("CORP_CIC"),
  	CORP_TYPE("CORP_TYPE"),
  	ECONOMIC_TYPE("ECONOMIC_TYPE"),
  	

	// ProductManager
    PROD_SC_GROUP("SC_GROUP"),
	PROD_AUDIT_ACTION("DATA_CHANGE_ACTION"),
	PROD_CHANNEL("CHANNEL"),
	PROD_CODE("PROD_CODE");
	//PRODUCT_GROUP_CATEGORY = "SC_GROUP";

	private final String value;

	CTCat(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
	
	public static CTCat from(String text) {
        for (CTCat b : CTCat.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
