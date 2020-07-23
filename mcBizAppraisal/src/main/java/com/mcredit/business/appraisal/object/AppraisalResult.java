package com.mcredit.business.appraisal.object;

public class AppraisalResult {
	
	private String rule;
	private Boolean result;
	private String description; 
	
	public AppraisalResult(String rule, Boolean result, String description) {
		this.rule = rule;
		this.result = result;
		this.description = description;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
