package com.mcredit.business.telesales.object;

public class SaveScoreObject {
	
	private String code;
	private String score;
	private String message;
	private String telcoCode;
	private String verifyInfo;
	
	public SaveScoreObject(String code, String score, String message, String telcoCode, String verifyInfo) {
		this.code = code;
		this.score = score;
		this.message = message;
		this.telcoCode = telcoCode;
		this.verifyInfo = verifyInfo;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTelcoCode() {
		return telcoCode;
	}
	public void setTelcoCode(String telcoCode) {
		this.telcoCode = telcoCode;
	}
	public String getVerifyInfo() {
		return verifyInfo;
	}
	public void setVerifyInfo(String verifyInfo) {
		this.verifyInfo = verifyInfo;
	}
	
}
