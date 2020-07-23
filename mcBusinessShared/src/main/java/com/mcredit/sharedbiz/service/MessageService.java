package com.mcredit.sharedbiz.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.mcredit.model.object.SendEmailDTO;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;


public class MessageService implements IMessage {

	private SendEmailDTO _email;
	private static final String SMTP_HOST_NAME = "172.16.21.101";
	public MessageService(SendEmailDTO email){
		this._email = email;	
	}
	
	@Override
	public void send() throws ValidationException {
		
		this.validate();
		
		Properties properties = System.getProperties();
		properties.setProperty("mail.transport.protocol", "smtp");
		properties.setProperty("mail.smtp.host", SMTP_HOST_NAME);
		properties.setProperty("mail.smtp.auth", "true");
		Authenticator auth = new SMTPAuthenticator(this._email.getSmtpUser(), this._email.getSmtpPassword());
		Session session = Session.getDefaultInstance(properties, auth);

		try {
			
			MimeMessage message = new MimeMessage(session);
			message.addHeader("Content-type", "text/HTML; charset=UTF-8");
			message.setFrom(new InternetAddress(_email.getFrom()));
			
			if (this._email.getTo() != null && !this._email.getTo().isEmpty()) {
				for (int i = 0; i < this._email.getTo().size(); i++) {
					message.addRecipient(Message.RecipientType.TO,new InternetAddress(this._email.getTo().get(i)));
				}
			}
				
			
			if (this._email.getTo() != null && (this._email.getCc() != null && !this._email.getCc().isEmpty())) {
				for (int i = 0; i < this._email.getCc().size(); i++) {
					message.addRecipient(Message.RecipientType.CC,new InternetAddress(this._email.getCc().get(i)));
				}

			}
				
			
			if (this._email.getTo() != null && (this._email.getBcc() != null && !this._email.getBcc().isEmpty())) {
				for (int i = 0; i < this._email.getBcc().size(); i++) {
					message.addRecipient(Message.RecipientType.BCC,new InternetAddress(this._email.getBcc().get(i)));
				}	
			}
				
			
			message.setSubject(_email.getSubject(), "utf-8");
		
			message.setContent(_email.getBody(), "text/html; charset=UTF-8");
			Transport.send(message);
			System.out.println("Sent Email message successfully....");
			
		} catch (MessagingException ex) {
			ex.printStackTrace();
			throw new ValidationException(ex.getMessage());
		}
		
	}
	
	private void validate() throws ValidationException{
		if(this._email == null)
			throw new ValidationException("Email information is required.");
		
		if(this._email.getTo() == null || this._email.getTo().isEmpty())
			throw new ValidationException("Email To is required.");
		
		if(StringUtils.isNullOrEmpty(this._email.getSubject()))
			throw new ValidationException("Email Subject is required.");
		
		if(StringUtils.isNullOrEmpty(this._email.getBody()))
			throw new ValidationException("Email Body is required.");
		
		if(StringUtils.isNullOrEmpty(this._email.getFrom()))
			throw new ValidationException("Email From is required.");
	}
	
	private class SMTPAuthenticator extends javax.mail.Authenticator {
		
		String smtpUserName = null;
		String smtpPassword = null;
		
		public SMTPAuthenticator(String smtpUsrName, String smtpPassword) {
			this.smtpUserName = smtpUsrName;
			this.smtpPassword = smtpPassword;
		}
		
		public PasswordAuthentication getPasswordAuthentication() {
			String username = this.smtpUserName;
			String password = this.smtpPassword;
			return new PasswordAuthentication(username, password);
		}
	}

}
