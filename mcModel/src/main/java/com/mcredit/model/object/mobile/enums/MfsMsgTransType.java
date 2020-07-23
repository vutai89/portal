package com.mcredit.model.object.mobile.enums;

public enum MfsMsgTransType {	
	MSG_TRANS_TYPE_CREATE_CASE_BPM("createLOSApplication"),
	MSG_TRANS_TYPE_ABORT_CASE_BPM("abortLOSApplication"),
	MSG_TRANS_TYPE_ROUTE_CASE_BPM("routeLOSApplication"),
	MSG_TRANS_TYPE_MFS_NOTIFICATION("notifyLOSApplication"),
	MSG_TRANS_TYPE_MFS_CHECK_CIC_MANUAL("checkCICManual")
	;

	private final String value;

	MfsMsgTransType(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}
