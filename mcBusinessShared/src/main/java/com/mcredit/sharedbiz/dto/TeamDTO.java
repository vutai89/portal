package com.mcredit.sharedbiz.dto;

import com.mcredit.data.telesale.entity.Team;

public class TeamDTO extends Team {
	private static final long serialVersionUID = 1L;
	String teamLeadName;

	public String getTeamLeadName() {
		return teamLeadName;
	}

	public void setTeamLeadName(String teamLeadName) {
		this.teamLeadName = teamLeadName;
	}

}
