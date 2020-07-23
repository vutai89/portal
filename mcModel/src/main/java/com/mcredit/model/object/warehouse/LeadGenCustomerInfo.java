package com.mcredit.model.object.warehouse;

import java.io.Serializable;

public class LeadGenCustomerInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6125869319176974787L;
	
	private String custName;
	private String identityNumber;
	private String mobilePhone;
	private String province;
	private String scoreRange;
	private String income;
	private String sourceInfo;
	private String productName;
	private String productCode;
	private String levelMin;
	private String levelMax;
	private String periodMin;
	private String periodMax;
	private String interestRateYear;
	private String interestRateMonth;
	private String watchListResult;
	
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getIdentityNumber() {
		return identityNumber;
	}
	public void setIdentityNumber(String identityNumber) {
		this.identityNumber = identityNumber;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getScoreRange() {
		return scoreRange;
	}
	public void setScoreRange(String scoreRange) {
		this.scoreRange = scoreRange;
	}
	public String getIncome() {
		return income;
	}
	public void setIncome(String income) {
		this.income = income;
	}
	public String getSourceInfo() {
		return sourceInfo;
	}
	public void setSourceInfo(String sourceInfo) {
		this.sourceInfo = sourceInfo;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getLevelMin() {
		return levelMin;
	}
	public void setLevelMin(String levelMin) {
		this.levelMin = levelMin;
	}
	public String getLevelMax() {
		return levelMax;
	}
	public void setLevelMax(String levelMax) {
		this.levelMax = levelMax;
	}
	public String getPeriodMin() {
		return periodMin;
	}
	public void setPeriodMin(String periodMin) {
		this.periodMin = periodMin;
	}
	public String getPeriodMax() {
		return periodMax;
	}
	public void setPeriodMax(String periodMax) {
		this.periodMax = periodMax;
	}
	public String getInterestRateYear() {
		return interestRateYear;
	}
	public void setInterestRateYear(String interestRateYear) {
		this.interestRateYear = interestRateYear;
	}
	public String getInterestRateMonth() {
		return interestRateMonth;
	}
	public void setInterestRateMonth(String interestRateMonth) {
		this.interestRateMonth = interestRateMonth;
	}
	public String getWatchListResult() {
		return watchListResult;
	}
	public void setWatchListResult(String watchListResult) {
		this.watchListResult = watchListResult;
	}
}
