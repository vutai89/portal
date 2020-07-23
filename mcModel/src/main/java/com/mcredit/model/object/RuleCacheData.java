package com.mcredit.model.object;

import java.util.List;

public class RuleCacheData {

	private String ruleKey;
	private String ruleMessage;
	private String ruleCode;
	private int ruleCombinationKey;
	private List<RuleDateType> dateParams;

	public String getRuleKey() {
		return ruleKey;
	}
	public void setRuleKey(String ruleKey) {
		this.ruleKey = ruleKey;
	}
	public String getRuleMessage() {
		return ruleMessage;
	}
	public void setRuleMessage(String ruleMessage) {
		this.ruleMessage = ruleMessage;
	}
	public String getRuleCode() {
		return ruleCode;
	}
	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}
	public List<RuleDateType> getDateParams() {
		return dateParams;
	}
	public void setDateParams(List<RuleDateType> dateParams) {
		this.dateParams = dateParams;
	}
	public int getRuleCombinationKey() {
		return ruleCombinationKey;
	}
	public void setRuleCombinationKey(int ruleCombinationKey) {
		this.ruleCombinationKey = ruleCombinationKey;
	}

}
