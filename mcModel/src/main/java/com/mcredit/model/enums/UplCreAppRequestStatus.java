package com.mcredit.model.enums;

public enum UplCreAppRequestStatus {

	N("N"),// New case upload (when insert)
	X("X"),// Receive files for new case completed (after extracted zip files)
	S("S"),//Sync Case to BPM success.
	P("P"),// Upload files for new case to BPM completed (Pending for appraisal)
	C("C"),// route case completed
	R("R"),// case in return status
	I("I"),// upload files but not sync to BPM
	T("T"),// Upload files for return case to BPM completed (Pending for appraisal)
	J("J"),// case was rejected
	A("A"),// case aborted
	V("V"),// case approved
	D("D"),// case was disbursed
	;

	private final String value;

	UplCreAppRequestStatus(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

}
