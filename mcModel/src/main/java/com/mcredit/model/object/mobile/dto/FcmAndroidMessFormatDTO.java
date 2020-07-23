package com.mcredit.model.object.mobile.dto;

public class FcmAndroidMessFormatDTO {
	FcmAndroidDataFormatDTO data;
	String to;
	public FcmAndroidDataFormatDTO getData() {
		return data;
	}
	public void setData(FcmAndroidDataFormatDTO data) {
		this.data = data;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public FcmAndroidMessFormatDTO(FcmAndroidDataFormatDTO data, String to) {
		super();
		this.data = data;
		this.to = to;
	}
}
