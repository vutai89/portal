package com.mcredit.model.object;

import java.util.List;
import java.util.Map;

public class RuleResult {
	private String returnCode = "200";
	private String ruleType;
	private String scalarValue;
	private String multiValue;
	private List<RuleOutputList> listValue;
	private List<Map<String, String>> dropList;

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	public String getScalarValue() {
		return scalarValue;
	}

	public void setScalarValue(String scalarValue) {
		this.scalarValue = scalarValue;
	}

	public String getMultiValue() {
		return multiValue;
	}

	public void setMultiValue(String multiValue) {
		this.multiValue = multiValue;
	}

	public List<RuleOutputList> getListValue() {
		return listValue;
	}

	public void setListValue(List<RuleOutputList> listValue) {
		this.listValue = listValue;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public List<Map<String, String>> getDropList() {
		return dropList;
	}

	public void setDropList(List<Map<String, String>> dropList) {
		this.dropList = dropList;
	}

}
