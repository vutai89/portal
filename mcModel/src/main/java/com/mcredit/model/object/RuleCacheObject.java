package com.mcredit.model.object;

public class RuleCacheObject {

	@ColumnIndex(index=0)
	private String ruleCode;
	@ColumnIndex(index=1)
	private Integer ruleId;
	@ColumnIndex(index=2)
	private Integer ruleCombinationKey;

	public String getRuleCode() {
		return ruleCode;
	}
	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}
	public Integer getRuleId() {
		return ruleId;
	}
	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}
	public Integer getRuleCombinationKey() {
		return ruleCombinationKey;
	}
	public void setRuleCombinationKey(Integer ruleCombinationKey) {
		this.ruleCombinationKey = ruleCombinationKey;
	}
	
}
