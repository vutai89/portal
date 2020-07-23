package com.mcredit.sharedbiz.validation;


public class ValidationException extends Exception {
	
	private static final long serialVersionUID = 176023227975132626L;
	
	private int HttpStatusCode = 400;
	private String code;
	
	private String status;
	private String reason;

	public ValidationException(String message) {
		super(message);
	}
	
	/*public ValidationException(String message, int statusCode) {
		super(message);
		this.HttpStatusCode = statusCode;
	}*/

	public ValidationException(String code, String message) {
		super(message);
		this.code = code;
	}
	
	public ValidationException(String status, String reason, String dateTime) {
		this.status = status;
		this.reason = reason;
	}

	public int getHttpStatusCode() {
		return HttpStatusCode;
	}

	public String getCode() {
		return code;
	}

	public String getStatus() {
		return status;
	}

	public String getReason() {
		return reason;
	}
}
