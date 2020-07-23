package com.mcredit.model.object;

import java.util.List;

public class RuleCacheMap {

	private String ruleKey;
	private RuleResult ruleOutput;
	private List<RuleDateType> dateParams;

	public String getRuleKey() {
		return ruleKey;
	}
	public void setRuleKey(String ruleKey) {
		this.ruleKey = ruleKey;
	}
	public RuleResult getRuleOutput() {
		return ruleOutput;
	}
	public void setRuleOutput(RuleResult ruleOutput) {
		this.ruleOutput = ruleOutput;
	}
	public List<RuleDateType> getDateParams() {
		return dateParams;
	}
	public void setDateParams(List<RuleDateType> dateParams) {
		this.dateParams = dateParams;
	}

}
