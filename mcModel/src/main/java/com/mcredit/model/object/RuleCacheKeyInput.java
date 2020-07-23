package com.mcredit.model.object;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class RuleCacheKeyInput {

	@ColumnIndex(index=0)
	private String ruleKey;
	@ColumnIndex(index=1)
	private String startDate;
	@ColumnIndex(index=2)
	private String endDate;
	private Map<String, String> paramValue;
	private List<RuleDateType> dateList;

	public String getRuleKey() {
		return ruleKey;
	}
	public void setRuleKey(String ruleKey) {
		this.ruleKey = ruleKey;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Map<String, String> getParamValue() {
		return paramValue;
	}
	public void setParamValue(Map<String, String> paramValue) {
		this.paramValue = paramValue;
	}
	public List<RuleDateType> getDateList() {
		return dateList;
	}
	public void setDateList(List<RuleDateType> dateList) {
		this.dateList = dateList;
	}
}
