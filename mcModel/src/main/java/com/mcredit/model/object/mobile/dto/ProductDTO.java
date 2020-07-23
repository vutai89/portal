package com.mcredit.model.object.mobile.dto;

public class ProductDTO {
	private Long id;
	
	private String productCode;
	
	private String productName;
	
	private Long minTenor;
	
	private Long maxTenor;
	
	private Long minLoanAmount;
	
	private Long maxLoanAmount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Long getMinTenor() {
		return minTenor;
	}

	public void setMinTenor(Long minTenor) {
		this.minTenor = minTenor;
	}

	public Long getMaxTenor() {
		return maxTenor;
	}

	public void setMaxTenor(Long maxTenor) {
		this.maxTenor = maxTenor;
	}

	public Long getMinLoanAmount() {
		return minLoanAmount;
	}

	public void setMinLoanAmount(Long minLoanAmount) {
		this.minLoanAmount = minLoanAmount;
	}

	public Long getMaxLoanAmount() {
		return maxLoanAmount;
	}

	public void setMaxLoanAmount(Long maxLoanAmount) {
		this.maxLoanAmount = maxLoanAmount;
	}
}
