package com.mcredit.model.object;

import java.util.ArrayList;

public class SendEmailDTO {
	private String smtpUser;
	private String smtpPassword;
	private String from;
	private ArrayList<String> to;
	private ArrayList<String> cc;
	private ArrayList<String> bcc;
	private String subject;
	private String body;
	
	
	public SendEmailDTO() {
		super();
	}

	public SendEmailDTO(String smtpUser, String smtpPassword, String from, ArrayList<String> to, ArrayList<String> cc,
			ArrayList<String> bcc, String subject, String body) {
		super();
		this.smtpUser = smtpUser;
		this.smtpPassword = smtpPassword;
		this.from = from;
		this.to = to;
		this.cc = cc;
		this.bcc = bcc;
		this.subject = subject;
		this.body = body;
	}

	public String getSmtpUser() {
		return smtpUser;
	}

	public void setSmtpUser(String smtpUser) {
		this.smtpUser = smtpUser;
	}

	public String getSmtpPassword() {
		return smtpPassword;
	}

	public void setSmtpPassword(String smtpPassword) {
		this.smtpPassword = smtpPassword;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public ArrayList<String> getTo() {
		return to;
	}

	public void setTo(ArrayList<String> to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public ArrayList<String> getCc() {
		return cc;
	}

	public void setCc(ArrayList<String> cc) {
		this.cc = cc;
	}

	public ArrayList<String> getBcc() {
		return bcc;
	}

	public void setBcc(ArrayList<String> bcc) {
		this.bcc = bcc;
	}

}
