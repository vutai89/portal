package com.mcredit.model.dto.assign;

public class ChangeUserStatusDTO {
	private Long userId;
	private Boolean active;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean isActive) {
		this.active = isActive;
	}

}
