package com.mcredit.model.dto.telesales;

import java.util.Date;

public class ScoreLGResult {
	
	private String scoreRange;
	private Date lastUpdatedDate;
	
	public String getScoreRange() {
		return scoreRange;
	}
	public void setScoreRange(String scoreRange) {
		this.scoreRange = scoreRange;
	}
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

}
