package com.mcredit.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ProductInterestDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String productCode;
	private String productName;
	private BigDecimal minTenor;
	private BigDecimal maxTenor;
	private BigDecimal minLoanAmount;
	private BigDecimal maxLoanAmount;
	private BigDecimal monthlyRate;
	private BigDecimal yearlyRate;
	
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public BigDecimal getMinTenor() {
		return minTenor;
	}
	public void setMinTenor(BigDecimal minTenor) {
		this.minTenor = minTenor;
	}
	public BigDecimal getMaxTenor() {
		return maxTenor;
	}
	public void setMaxTenor(BigDecimal maxTenor) {
		this.maxTenor = maxTenor;
	}
	public BigDecimal getMinLoanAmount() {
		return minLoanAmount;
	}
	public void setMinLoanAmount(BigDecimal minLoanAmount) {
		this.minLoanAmount = minLoanAmount;
	}
	public BigDecimal getMaxLoanAmount() {
		return maxLoanAmount;
	}
	public void setMaxLoanAmount(BigDecimal maxLoanAmount) {
		this.maxLoanAmount = maxLoanAmount;
	}
	public BigDecimal getMonthlyRate() {
		return monthlyRate;
	}
	public void setMonthlyRate(BigDecimal monthlyRate) {
		this.monthlyRate = monthlyRate;
	}
	public BigDecimal getYearlyRate() {
		return yearlyRate;
	}
	public void setYearlyRate(BigDecimal yearlyRate) {
		this.yearlyRate = yearlyRate;
	}
}
