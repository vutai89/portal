package com.mcredit.model.dto.assign;

public class ExternalDTO {
	
	private String id;
	private String userName;

	public ExternalDTO(String id, String userName) {
		this.id = id;
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
