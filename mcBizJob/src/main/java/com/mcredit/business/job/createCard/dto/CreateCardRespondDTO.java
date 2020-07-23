package com.mcredit.business.job.createCard.dto;

public class CreateCardRespondDTO {
	private int fetch;
	private int success;
	private int error;
	private int ignore;
	
	public CreateCardRespondDTO() {
		this.fetch=0;
		this.success=0;
		this.error=0;
		this.ignore=0;

	}
	
	public int getFetch() {
		return fetch;
	}
	public void setFetch(int fetch) {
		this.fetch = fetch;
	}
	public int getSuccess() {
		return success;
	}
	public void setSuccess(int success) {
		this.success = success;
	}
	public int getError() {
		return error;
	}
	public void setError(int error) {
		this.error = error;
	}
	public int getIgnore() {
		return ignore;
	}
	public void setIgnore(int ingore) {
		this.ignore = ingore;
	}
}
