package com.mcredit.model.object.gendoc;

public class GendocRepaymentScheduleView {
	private Integer teror;
	private String dateOfPayment;
	private String principalAndMonthlyInterest;
	private String originalAmount;
	private String profitAmount;
	private String collectionServiceFee;
	private String payablesMonthly;

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

	public String getPrincipalAndMonthlyInterest() {
		return principalAndMonthlyInterest;
	}

	public void setPrincipalAndMonthlyInterest(String principalAndMonthlyInterest) {
		this.principalAndMonthlyInterest = principalAndMonthlyInterest;
	}

	public String getOriginalAmount() {
		return originalAmount;
	}

	public void setOriginalAmount(String originalAmount) {
		this.originalAmount = originalAmount;
	}

	public String getProfitAmount() {
		return profitAmount;
	}

	public void setProfitAmount(String profitAmount) {
		this.profitAmount = profitAmount;
	}

	public String getCollectionServiceFee() {
		return collectionServiceFee;
	}

	public void setCollectionServiceFee(String collectionServiceFee) {
		this.collectionServiceFee = collectionServiceFee;
	}

	public String getPayablesMonthly() {
		return payablesMonthly;
	}

	public void setPayablesMonthly(String payablesMonthly) {
		this.payablesMonthly = payablesMonthly;
	}

	public GendocRepaymentScheduleView(Integer teror, String dateOfPayment, String principalAndMonthlyInterest,
			String originalAmount, String profitAmount, String collectionServiceFee, String payablesMonthly) {
		super();
		this.teror = teror;
		this.dateOfPayment = dateOfPayment;
		this.principalAndMonthlyInterest = principalAndMonthlyInterest;
		this.originalAmount = originalAmount;
		this.profitAmount = profitAmount;
		this.collectionServiceFee = collectionServiceFee;
		this.payablesMonthly = payablesMonthly;
	}

	public GendocRepaymentScheduleView() {
		super();
	}

}