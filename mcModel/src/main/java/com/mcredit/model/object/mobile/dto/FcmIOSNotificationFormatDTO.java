package com.mcredit.model.object.mobile.dto;

public class FcmIOSNotificationFormatDTO {

	private String title;
	private String subtitle;
	private String body;
	
	
	public FcmIOSNotificationFormatDTO(String title, String subtitle, String body) {
		super();
		this.title = title;
		this.subtitle = subtitle;
		this.body = body;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	
	
}
