package com.mcredit.model.dto.common;

import java.io.Serializable;

public class TemplateDTO implements Serializable {
	
	private static final long serialVersionUID = -2723711928497744538L;

	private String code;
	
	private String subject;
	
	private String template;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

}
