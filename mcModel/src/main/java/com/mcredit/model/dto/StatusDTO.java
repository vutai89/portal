package com.mcredit.model.dto;

public class StatusDTO {

	private String serverName;
	private String statusDB;
	private String message;

	public StatusDTO() {
	}
	
	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getStatusDB() {
		return statusDB;
	}

	public void setStatusDB(String statusDB) {
		this.statusDB = statusDB;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
