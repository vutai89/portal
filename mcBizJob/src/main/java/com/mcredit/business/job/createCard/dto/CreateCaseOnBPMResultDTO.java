package com.mcredit.business.job.createCard.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateCaseOnBPMResultDTO {
	 
	
	
	@SerializedName("code")
	@Expose
	private String code;

	@SerializedName("message")
	@Expose
	private String message;

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

	public String getName(){		
		return "error";
	}
	
	public String toJson(){
		return "{" 
				+ (code != null ? "\"code\":\"" + code  : "")
				+ (message != null ? "\", \"message\":\"" + message : "") + "\"}";
	}

}
