package com.mcredit.model.enums.disaudit;

public enum AuditEnum {
	
	SFTP("sftp"),
	FTP("ftp"),
	FTPS("ftps"),
	DDMMYYYY("{0}"),
	THU("0"),
	CHI("1"),
	SUCCESS("1"),
	FAIL("0"),
	WAIT_PERMISSION_FAIL("2"),
	YES(1),
	EQUAL("0"),
	NOT_EXIST_MC("1"),
	NOT_EXIST_THIRD_PARTY("2"),
	NOT_EQUAL_MONEY("3"),
	NOT_EQUAL_STATUS("4"),
	NOT_EQUAL_CONTRACT_ID("5"),
	NOT_EQUAL_AUDIT_DATE("6"),
	NOT_EQUAL_TYPE("7"), 
	DUPLICATE("8"),
	TONG_HOP("0"),
	BAO_CAO_CHI_HO("1"),
	BAO_CAO_THU_HO("2"), 
	VNPOST17("VNPOST17"),
	TIME_CONTROL_17h("17h"),
	TIME_CONTROL_24h("24h"),
	EXIST(1),
	STATUS("status"),
	TOTAL("total"),
	TIMES_NEW_ROMAN("Times New Roman"),
	INSTALLMENT_LOAN("0")
	;
	
	private final Object value;
	
	AuditEnum(Object value) {
		this.value = value;
	}
	
	public String value() {
		return (String) this.value;
	}
	
	public Long longValue() {
		return (Long)this.value;
	}
	
	public Integer intValue() {
		return (Integer)this.value;
	}
}
