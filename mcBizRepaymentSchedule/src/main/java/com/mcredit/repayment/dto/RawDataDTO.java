package com.mcredit.repayment.dto;

import java.util.Date;

public class RawDataDTO {
	private Integer teror;
	private Date dateOfPayment;
	private Double principalAndMonthlyInterest;
	private Double originalAmount;
	private Double profitAmount;
	private Double debt;
	public Integer getTeror() {
		return teror;
	}
	public void setTeror(Integer teror) {
		this.teror = teror;
	}
	public Date getDateOfPayment() {
		return dateOfPayment;
	}
	public void setDateOfPayment(Date dateOfPayment) {
		this.dateOfPayment = dateOfPayment;
	}
	public Double getPrincipalAndMonthlyInterest() {
		return principalAndMonthlyInterest;
	}
	public void setPrincipalAndMonthlyInterest(Double principalAndMonthlyInterest) {
		this.principalAndMonthlyInterest = principalAndMonthlyInterest;
	}
	public Double getOriginalAmount() {
		return originalAmount;
	}
	public void setOriginalAmount(Double originalAmount) {
		this.originalAmount = originalAmount;
	}
	public Double getProfitAmount() {
		return profitAmount;
	}
	public void setProfitAmount(Double profitAmount) {
		this.profitAmount = profitAmount;
	}
	public Double getDebt() {
		return debt;
	}
	public void setDebt(Double debt) {
		this.debt = debt;
	}
	
	
}
