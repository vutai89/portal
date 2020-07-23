package com.mcredit.model.object;

import java.math.BigDecimal;

public class PaymentOnWebInfo {

	private String contractNumber;
	private String identityNumber;
	private String customerName;
	private String valueDate;
	private BigDecimal loanAmount;
	private Integer interestRate;
	private Integer tenor;
	private Long period;
	private String periodDate;
	private BigDecimal periodAmount;
	private BigDecimal principalAmount;
	private BigDecimal interestAmount;
	private BigDecimal feeAmount;
	private BigDecimal totalAmount;
	private String lastUpdated;
	
	public String getContractNumber() {
		return contractNumber;
	}
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
	public String getIdentityNumber() {
		return identityNumber;
	}
	public void setIdentityNumber(String identityNumber) {
		this.identityNumber = identityNumber!=null?identityNumber:"";
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getValueDate() {
		return valueDate;
	}
	public void setValueDate(String valueDate) {
		this.valueDate = valueDate;
	}
	public BigDecimal getLoanAmount() {
		return loanAmount;
	}
	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}
	public Integer getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(Integer interestRate) {
		this.interestRate = interestRate;
	}
	public Integer getTenor() {
		return tenor;
	}
	public void setTenor(Integer tenor) {
		this.tenor = tenor;
	}
	public Long getPeriod() {
		return period;
	}
	public void setPeriod(Long period) {
		this.period = period;
	}
	public String getPeriodDate() {
		return periodDate;
	}
	public void setPeriodDate(String periodDate) {
		this.periodDate = periodDate;
	}
	public BigDecimal getPeriodAmount() {
		return periodAmount;
	}
	public void setPeriodAmount(BigDecimal periodAmount) {
		this.periodAmount = periodAmount;
	}
	public BigDecimal getPrincipalAmount() {
		return principalAmount;
	}
	public void setPrincipalAmount(BigDecimal principalAmount) {
		this.principalAmount = principalAmount;
	}
	public BigDecimal getInterestAmount() {
		return interestAmount;
	}
	public void setInterestAmount(BigDecimal interestAmount) {
		this.interestAmount = interestAmount;
	}
	public BigDecimal getFeeAmount() {
		return feeAmount;
	}
	public void setFeeAmount(BigDecimal feeAmount) {
		this.feeAmount = feeAmount;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
}