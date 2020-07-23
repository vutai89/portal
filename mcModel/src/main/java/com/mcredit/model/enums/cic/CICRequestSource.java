package com.mcredit.model.enums.cic;

public enum CICRequestSource {
	
	MOBILE(0),
	WEB_PORTAL(1),
	WEB_REPORT_ERROR(2),		// CIC loi, khong tra duoc ket qua
	WEB_REPORT_WRONG(3),		// CIC nhap ket qua sai
	THIRD_PARTY(4);
	
	private final Integer value;
	CICRequestSource(Integer value) {
		this.value = value;
	}
	
	public Integer value() {
		return this.value;
	}
	
	public static CICRequestSource from(Integer source) {
        for (CICRequestSource b : CICRequestSource.values()) {
            if (b.value.equals(source)) {
                return b;
            }
        }
        return null;
    }

}
