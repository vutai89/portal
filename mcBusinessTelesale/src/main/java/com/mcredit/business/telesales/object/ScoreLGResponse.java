package com.mcredit.business.telesales.object;

public class ScoreLGResponse {

	private int status;
	private String message;
	private String minScore;
	private String maxScore;
	private String dateTime;
	
	public ScoreLGResponse(int status, String message, String minScore, String maxScore, String dateTime) {
		this.status = status;
		this.message = message;
		this.minScore = minScore;
		this.maxScore = maxScore;
		this.dateTime = dateTime;
	}
	
	public int getStatus() {
		return status;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMinScore() {
		return minScore;
	}
	public void setMinScore(String minScore) {
		this.minScore = minScore;
	}
	public String getMaxScore() {
		return maxScore;
	}
	public void setMaxScore(String maxScore) {
		this.maxScore = maxScore;
	}
	
	
}
