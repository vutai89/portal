package com.mcredit.model.object.mobile.dto;

public class FcmAndroidDataFormatDTO {
	String title;
	String body;
	String type;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public FcmAndroidDataFormatDTO(String title, String body, String type) {
		super();
		this.title = title;
		this.body = body;
		this.type = type;
	}
}
