package com.mcredit.data.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;


@Entity
@Table(name="SEND_EMAIL")
public class SendEmail implements Serializable {
	
	private static final long serialVersionUID = -6600569185141115288L;

	@Id
	@GenericGenerator(name = "seq_send_email" , strategy = "com.mcredit.data.common.entity.SequenceGenerate" , parameters = @Parameter(name = "seqName", value = "SEQ_SEND_EMAIL_ID.NEXTVAL"))
	@GeneratedValue(generator = "seq_send_email")
	private Long id;
	
	@Column(name="EMAIL_TO")
	private String emailTo;
	
	@Column(name="CC")
	private String cc;
	
	@Column(name="BCC")
	private String bcc;
	
	@Column(name="SUBJECT")
	private String subject;
	
	@Column(name="BODY")
	private String body;
	
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="SEND_STATUS")
	private Long sendStatus;
	
	@Column(name="ERR_MSG")
	private String errorMsg;
	
	@Column(name = "EMAIL_TYPE")
	private Long emailType;
	
	@Column(name="RECORD_STATUS")
	private String recordStatus;

	public SendEmail() {
	}
	
	public SendEmail(String emailTo, String cc, String subject, String body, String createdBy, Long idSendStatus) {
		this.emailTo = emailTo;
		this.cc = cc;
		this.subject = subject;
		this.body = body;
		this.createdBy = createdBy;
		this.sendStatus = idSendStatus;
		this.createdDate = new Date();
		this.recordStatus = "A";
	}
	
	public SendEmail(String emailTo, String cc, String bcc, String subject, String body, String createdBy, String errorMsg, Long idEmailType, Long idSendStatus) {
		this.emailTo = emailTo;
		this.cc = cc;
		this.bcc = bcc;
		this.subject = subject;
		this.body = body;
		this.createdBy = createdBy;
		this.sendStatus = idSendStatus;
		this.createdDate = new Date();
		this.errorMsg = errorMsg;
		this.emailType = idEmailType;
		this.recordStatus = "A";
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmailTo() {
		return emailTo;
	}

	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Long getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(Long sendStatus) {
		this.sendStatus = sendStatus;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Long getEmailType() {
		return emailType;
	}

	public void setEmailType(Long emailType) {
		this.emailType = emailType;
	}
}
