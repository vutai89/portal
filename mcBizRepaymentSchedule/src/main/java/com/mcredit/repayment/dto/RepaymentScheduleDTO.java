package com.mcredit.repayment.dto;

import java.util.Date;

public class RepaymentScheduleDTO {
	private Integer teror;
	private String dateOfPayment;
	private Double principalAndMonthlyInterest;
	private Double originalAmount;
	private Double profitAmount;
	private Double collectionServiceFee;
	private Double payablesMonthly;
	public Integer getTeror() {
		return teror;
	}
	public void setTeror(Integer teror) {
		this.teror = teror;
	}
	
	public String getDateOfPayment() {
		return dateOfPayment;
	}
	public void setDateOfPayment(String dateOfPayment) {
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
	public Double getCollectionServiceFee() {
		return collectionServiceFee;
	}
	public void setCollectionServiceFee(Double collectionServiceFee) {
		this.collectionServiceFee = collectionServiceFee;
	}
	public Double getPayablesMonthly() {
		return payablesMonthly;
	}
	public void setPayablesMonthly(Double payablesMonthly) {
		this.payablesMonthly = payablesMonthly;
	}
}
