package com.mcredit.business.telesales.util;

public class ConstantTelesale {
	public static String CUS_HIS_REFID_MARK_TS = "MARK_TS";
	public static String CUS_HIS_REFID_MARK_ESB = "MARK_ESB";
	public static String CUS_HIS_REFID_OTP = "SEND_OTP";
	public static String CUS_HIS_CODE_SUCCESS = "success";
	
	public static Integer SCORING_STATUS_SUCCESS_INVALID_800 = 800; //800: Thanh cong, Diem Ts khong qua 30 ngay
	public static Integer SCORING_STATUS_SUCCESS_VALID_801 = 801; //801: Thanh cong, diem ts qua 30 ngay
	public static Integer SCORING_STATUS_FAIL_NOT_FOUND_802 = 802; // 802: That bai do khong co diem tren CRM
	public static Integer SCORING_STATUS_FAIL_ORTHER_803 = 803; // 803: That bai do nguyen nhan khac
	
	// System name
	public static String BPM = "BPM";
	public static String CRM = "CRM";
	
	// RQ 1013:
	public static Integer STATUS_800 = 800; // 800: Thanh cong, Diem Ts khong qua 30 ngay
	public static Integer STATUS_801 = 801; // 801: Thanh cong, diem ts qua 30 ngay
	public static Integer STATUS_802 = 802;	// 802: That bai do khong co diem tren CRM
	public static Integer STATUS_803 = 803; // 803: That bai do nguyen nhan khac
	
	// RQ RQ1010:
	public static Integer LEADGEN_TYPE = 1;
	public static Integer SCORING_TYPE = 2;
	
	// Get Product Rule
	public static String VIETTEL_TELCOCODE = "Viettel";
	public static String VINAPHONE_TELCOCODE = "Vina";
	public static String VINAPHONE_TELCOCODE2 = "Vinaphone";
	public static String MOBIFONE_TELCOCODE = "Mobifone";
	public static String CICDATA_PARTNER = "CICData";
	public static String TS_PARTNER = "TS";
	
	// credit_bureau_data CODE
	public static String SCORE_CB_SOURCE = "T";
	public static String CRM_CBTYPE = "C";
	public static String BPM_CBTYPE = "B";
	
}
