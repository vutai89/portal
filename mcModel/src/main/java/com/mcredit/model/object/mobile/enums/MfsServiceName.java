package com.mcredit.model.object.mobile.enums;

public enum MfsServiceName {
	SERVICE_CREATE_CASE_BPM("create_case"),
	SERVICE_ABORT_CASE_BPM("abort_case"),
	SERVICE_ROUTE_CASE_BPM("route_case"),
	;

	private final String value;

	MfsServiceName(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}
