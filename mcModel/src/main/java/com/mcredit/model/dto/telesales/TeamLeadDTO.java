package com.mcredit.model.dto.telesales;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeamLeadDTO {
	@SerializedName("isTeamLead")
	@Expose
	private boolean isTeamLead ;

	public boolean isTeamLead() {
		return isTeamLead;
	}

	public void setTeamLead(boolean isTeamLead) {
		this.isTeamLead = isTeamLead;
	}
	
	
}
