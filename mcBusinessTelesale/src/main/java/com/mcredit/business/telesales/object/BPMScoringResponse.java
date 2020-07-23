package com.mcredit.business.telesales.object;

import java.util.List;

public class BPMScoringResponse {
	
	private String returnCode;
	private String returnMes;

	private List<BPMScoringObject> scoreList;
	
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnMes() {
		return returnMes;
	}
	public void setReturnMes(String returnMes) {
		this.returnMes = returnMes;
	}
	public List<BPMScoringObject> getScoreList() {
		return scoreList;
	}
	public void setScoreList(List<BPMScoringObject> scoreList) {
		this.scoreList = scoreList;
	}
	
}
