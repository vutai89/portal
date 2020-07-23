package com.mcredit.model.object;

import java.util.Date;

public class RuleDateType {

	private String varName;
	private String paramName;
	private String startDate;
	private String endDate;
	private Date dtStartDate;
	private Date dtEndDate;

	public String getVarName() {
		return varName;
	}
	public void setVarName(String varName) {
		this.varName = varName;
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
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public Date getDtStartDate() {
		return dtStartDate;
	}
	public void setDtStartDate(Date dtStartDate) {
		this.dtStartDate = dtStartDate;
	}
	public Date getDtEndDate() {
		return dtEndDate;
	}
	public void setDtEndDate(Date dtEndDate) {
		this.dtEndDate = dtEndDate;
	}

}
