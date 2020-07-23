package com.mcredit.model.object;

import java.math.BigDecimal;

public class CreditContractInfo {

	private String contractNumber;
	private String typeOfLoan;
	private String identityNumber;
	private BigDecimal outstandingBalance;
	private String signDate;
	private String status;
	private String customerName;
	private String mobilePhone;
	private BigDecimal originalAmount;
	private Integer tenor;
	private Integer interestRate;
	private String valueDate;
	private String maturityDate;
	private String disbursementChannel;
	private Integer nextPayment;
	private String nextPaymentDate;
	private BigDecimal nextPaymentAmount;
	private BigDecimal totalOverdueAmount;
	private BigDecimal totalAmount;
	private String lastUpdated;

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
	
	public String getTypeOfLoan() {
		return typeOfLoan;
	}

	public void setTypeOfLoan(String typeOfLoan) {
		this.typeOfLoan = typeOfLoan;
	}

	public String getIdentityNumber() {
		return identityNumber;
	}

	public void setIdentityNumber(String identityNumber) {
		this.identityNumber = identityNumber != null ? identityNumber : "";
	}

	public BigDecimal getOutstandingBalance() {
		return outstandingBalance;
	}

	public void setOutstandingBalance(BigDecimal outstandingBalance) {
		this.outstandingBalance = outstandingBalance;
	}

	public String getSignDate() {
		return signDate;
	}

	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public BigDecimal getOriginalAmount() {
		return originalAmount;
	}

	public void setOriginalAmount(BigDecimal originalAmount) {
		this.originalAmount = originalAmount;
	}

	public Integer getTenor() {
		return tenor;
	}

	public void setTenor(Integer tenor) {
		this.tenor = tenor;
	}

	public Integer getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(Integer interestRate) {
		this.interestRate = interestRate;
	}

	public String getValueDate() {
		return valueDate;
	}

	public void setValueDate(String valueDate) {
		this.valueDate = valueDate;
	}

	public String getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(String maturityDate) {
		this.maturityDate = maturityDate;
	}

	public String getDisbursementChannel() {
		return disbursementChannel;
	}

	public void setDisbursementChannel(String disbursementChannel) {
		this.disbursementChannel = disbursementChannel != null ? disbursementChannel : "";
	}

	public Integer getNextPayment() {
		return nextPayment;
	}

	public void setNextPayment(Integer nextPayment) {
		this.nextPayment = nextPayment;
	}

	public String getNextPaymentDate() {
		return nextPaymentDate;
	}

	public void setNextPaymentDate(String nextPaymentDate) {
		this.nextPaymentDate = nextPaymentDate;
	}

	public BigDecimal getNextPaymentAmount() {
		return nextPaymentAmount;
	}

	public void setNextPaymentAmount(BigDecimal nextPaymentAmount) {
		this.nextPaymentAmount = nextPaymentAmount;
	}

	public BigDecimal getTotalOverdueAmount() {
		return totalOverdueAmount;
	}

	public void setTotalOverdueAmount(BigDecimal totalOverdueAmount) {
		this.totalOverdueAmount = totalOverdueAmount;
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