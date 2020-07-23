package com.mcredit.model.object.mobile.dto;

public class SendNoteDTO {
	private String note_content;
	private int send_mail;
	
	public SendNoteDTO(String note_content, int send_mail) {
		super();
		this.note_content = note_content;
		this.send_mail = send_mail;
	}

	public String getNote_content() {
		return note_content;
	}
	public void setNote_content(String note_content) {
		this.note_content = note_content;
	}
	public int getSend_mail() {
		return send_mail;
	}
	public void setSend_mail(int send_mail) {
		this.send_mail = send_mail;
	}

}
