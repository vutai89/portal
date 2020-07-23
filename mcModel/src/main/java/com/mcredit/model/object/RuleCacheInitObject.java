package com.mcredit.model.object;

public class RuleCacheInitObject {

	@ColumnIndex(index=0)
	private Integer ruleId;
	@ColumnIndex(index=1)
	private String paramType;
	@ColumnIndex(index=2)
	private String paramName;
	@ColumnIndex(index=3)
	private String bindVar;
	@ColumnIndex(index=4)
	private String dataFormat;
	@ColumnIndex(index=5)
	private String dataType;
	@ColumnIndex(index=6)
	private String paramListType;
	private String varName;
	private String methodName;
	private String methodGetName;
	private int paramIndex;
	
	public RuleCacheInitObject() {
		this.paramIndex = -1;
	}

	public Integer getRuleId() {
		return ruleId;
	}
	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}
	public String getParamType() {
		return paramType;
	}
	public void setParamType(String paramType) {
		this.paramType = paramType;
	}
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public String getBindVar() {
		return bindVar;
	}
	public void setBindVar(String bindVar) {
		this.bindVar = bindVar;
	}
	public String getVarName() {
		return varName;
	}
	public void setVarName(String varName) {
		this.varName = varName;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getDataFormat() {
		return dataFormat;
	}
	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public int getParamIndex() {
		return paramIndex;
	}
	public void setParamIndex(int paramIndex) {
		this.paramIndex = paramIndex;
	}

	public String getMethodGetName() {
		return methodGetName;
	}

	public void setMethodGetName(String methodGetName) {
		this.methodGetName = methodGetName;
	}

	public String getParamListType() {
		return paramListType;
	}

	public void setParamListType(String paramListType) {
		this.paramListType = paramListType;
	}
	
}
