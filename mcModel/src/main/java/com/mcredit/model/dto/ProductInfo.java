package com.mcredit.model.dto;

public class ProductInfo {
	
	private Long maxLoanAmount;
	private Long minLoanAmount;
	private Long maxTenor;
	private Long minTenor;
	
	public ProductInfo(Long maxLoanAmount, Long minLoanAmount, Long maxTenor, Long minTenor) {
		this.maxLoanAmount = maxLoanAmount;
		this.minLoanAmount = minLoanAmount;
		this.maxTenor = maxTenor;
		this.minTenor = minTenor;
	}

	public Long getMaxLoanAmount() {
		return maxLoanAmount;
	}
	
	public void setMaxLoanAmount(Long maxLoanAmount) {
		this.maxLoanAmount = maxLoanAmount;
	}

	public Long getMinLoanAmount() {
		return minLoanAmount;
	}

	public void setMinLoanAmount(Long minLoanAmount) {
		this.minLoanAmount = minLoanAmount;
	}

	public Long getMaxTenor() {
		return maxTenor;
	}

	public void setMaxTenor(Long maxTenor) {
		this.maxTenor = maxTenor;
	}

	public Long getMinTenor() {
		return minTenor;
	}

	public void setMinTenor(Long minTenor) {
		this.minTenor = minTenor;
	}
	
}
