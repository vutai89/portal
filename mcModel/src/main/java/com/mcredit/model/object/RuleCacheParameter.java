package com.mcredit.model.object;

public class RuleCacheParameter {

	@ColumnIndex(index=0)
	private Integer id;
	private String paramName;
	@ColumnIndex(index=3)
	private String paramValue;
	@ColumnIndex(index=1)
	private String paramType;
	@ColumnIndex(index=2)
	private String paramDataType;
	@ColumnIndex(index=4)
	private String paramDetailName;
	private String paramActualValue;

	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public String getParamValue() {
		return paramValue;
	}
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	public String getParamType() {
		return paramType;
	}
	public void setParamType(String paramType) {
		this.paramType = paramType;
	}
	public String getParamDetailName() {
		return paramDetailName;
	}
	public void setParamDetailName(String paramDetailName) {
		this.paramDetailName = paramDetailName;
	}
	public String getParamActualValue() {
		return paramActualValue;
	}
	public void setParamActualValue(String paramActualValue) {
		this.paramActualValue = paramActualValue;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getParamDataType() {
		return paramDataType;
	}
	public void setParamDataType(String paramDataType) {
		this.paramDataType = paramDataType;
	}

}
