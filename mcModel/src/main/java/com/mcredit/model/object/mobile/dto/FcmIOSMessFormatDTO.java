package com.mcredit.model.object.mobile.dto;

public class FcmIOSMessFormatDTO {
	private String to;
	private Boolean mutable_content;
	private FcmIOSMessDataFormatDTO data;
	private FcmIOSNotificationFormatDTO notification;
	
	public FcmIOSMessFormatDTO(String to, Boolean mutable_content, FcmIOSMessDataFormatDTO data,
			FcmIOSNotificationFormatDTO notification) {
		super();
		this.to = to;
		this.mutable_content = mutable_content;
		this.data = data;
		this.notification = notification;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public Boolean getMutable_content() {
		return mutable_content;
	}
	public void setMutable_content(Boolean mutable_content) {
		this.mutable_content = mutable_content;
	}
	public FcmIOSMessDataFormatDTO getData() {
		return data;
	}
	public void setData(FcmIOSMessDataFormatDTO data) {
		this.data = data;
	}
	public FcmIOSNotificationFormatDTO getNotification() {
		return notification;
	}
	public void setNotification(FcmIOSNotificationFormatDTO notification) {
		this.notification = notification;
	}
	
	
	

}
